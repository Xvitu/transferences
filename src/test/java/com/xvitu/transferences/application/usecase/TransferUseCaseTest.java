package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.TransferenceNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.enums.NotificationEventEnum;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.domain.vo.ValidatedTransference;
import com.xvitu.transferences.infrastructure.gateway.authorization.AuthorizationGateway;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationData;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationEvent;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {

    @Mock
    private TransferenceValidator validator;

    @Mock
    private TransferenceDataProvider transferenceDataProvider;

    @Mock
    private AuthorizationGateway authorizationGateway;

    @Mock
    private WalletDataProvider walletDataProvider;

    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private TransferUseCase useCase;

    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final UUID TRANSFERENCE_ID = UUID.randomUUID();
    private static final BigDecimal AMOUNT = new BigDecimal("100.00");

    private TransferCommand command;
    private Transference pendingTransference;

    @BeforeEach
    void setup() {
        pendingTransference = Transference.pending(AMOUNT, PAYER_ID, PAYEE_ID);
        command = new TransferCommand(TRANSFERENCE_ID, PAYER_ID, PAYEE_ID, AMOUNT);

        when(transferenceDataProvider.findById(TRANSFERENCE_ID))
                .thenReturn(Optional.of(pendingTransference));
    }

    @Test
    void execute_ShouldUpdateTransferenceToSuccess_AndAdjustWallets_AndNotify_WhenAuthorizationPasses() {
        AuthorizationData authData = new AuthorizationData(true);
        when(authorizationGateway.get()).thenReturn(Mono.just(new AuthorizationResponse("success", authData)));

        ValidatedTransference validated = mock(ValidatedTransference.class);
        Wallet payerWallet = mock(Wallet.class);
        Wallet updatedPayerWallet = mock(Wallet.class);
        Wallet payeeWallet = mock(Wallet.class);
        Wallet updatedPayeeWallet = mock(Wallet.class);
        User payer = mock(User.class);
        User payee = mock(User.class);

        when(validator.validate(PAYER_ID, PAYEE_ID, AMOUNT)).thenReturn(validated);
        when(validated.payeerWallet()).thenReturn(payerWallet);
        when(validated.payeeWallet()).thenReturn(payeeWallet);
        when(validated.payer()).thenReturn(payer);
        when(validated.payee()).thenReturn(payee);

        when(validated.payeerWallet()).thenReturn(payerWallet);
        when(validated.payeeWallet()).thenReturn(payeeWallet);

        when(payerWallet.withdraw(any())).thenReturn(updatedPayerWallet);
        when(payeeWallet.deposit(any())).thenReturn(updatedPayeeWallet);

        useCase.execute(command);

        verify(walletDataProvider).save(updatedPayerWallet);
        verify(walletDataProvider).save(updatedPayeeWallet);

        verify(validator, times(1)).validate(PAYER_ID, PAYEE_ID, AMOUNT);

        ArgumentCaptor<Transference> transferenceCaptor = ArgumentCaptor.forClass(Transference.class);
        verify(transferenceDataProvider, times(1)).save(transferenceCaptor.capture());

        Transference saved = transferenceCaptor.getValue();
        assertEquals(pendingTransference.success().status(), saved.status());

        ArgumentCaptor<NotificationEvent> notifyCaptor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(notificationPublisher, times(2)).publish(notifyCaptor.capture());

        NotificationEvent first = notifyCaptor.getAllValues().get(0);
        NotificationEvent second = notifyCaptor.getAllValues().get(1);

        assertEquals(payer.getEmail(), first.email());
        assertEquals(NotificationEventEnum.TRANSFERENCE_SEND.toString(), first.event());

        assertEquals(payee.getEmail(), second.email());
        assertEquals(NotificationEventEnum.TRANSFERENCE_RECEIVED.toString(), second.event());
    }

    @Test
    void execute_ShouldUpdateTransferenceToFail_AndAdjustWallets_AndNotify_WhenAuthorizationFails() {
        AuthorizationData authData = new AuthorizationData(false);
        when(authorizationGateway.get()).thenReturn(Mono.just(new AuthorizationResponse("fail", authData)));

        ValidatedTransference validated = mock(ValidatedTransference.class);
        User payer = mock(User.class);
        User payee = mock(User.class);

        when(validator.validate(PAYER_ID, PAYEE_ID, AMOUNT)).thenReturn(validated);
        when(validated.payer()).thenReturn(payer);
        when(validated.payee()).thenReturn(payee);

        useCase.execute(command);

        verify(walletDataProvider, times(0)).save(any());

        ArgumentCaptor<Transference> transferenceCaptor = ArgumentCaptor.forClass(Transference.class);
        verify(transferenceDataProvider, times(1)).save(transferenceCaptor.capture());

        Transference saved = transferenceCaptor.getValue();
        assertEquals(pendingTransference.fail().status(), saved.status());

        ArgumentCaptor<NotificationEvent> notifyCaptor = ArgumentCaptor.forClass(NotificationEvent.class);
        verify(notificationPublisher, times(1)).publish(notifyCaptor.capture());

        NotificationEvent event = notifyCaptor.getValue();
        assertEquals(payer.getEmail(), event.email());
        assertEquals(NotificationEventEnum.TRANSFERENCE_FAILED.toString(), event.event());
    }

    @Test
    void execute_ShouldThrow_WhenTransferenceNotFound() {
        when(transferenceDataProvider.findById(TRANSFERENCE_ID)).thenReturn(Optional.empty());

        assertThrows(TransferenceNotFoundException.class, () -> useCase.execute(command));

        verify(validator, never()).validate(anyInt(), anyInt(), any());
        verify(walletDataProvider, never()).save(any());
        verify(transferenceDataProvider, never()).save(any());
        verify(notificationPublisher, never()).publish(any());
    }
}
