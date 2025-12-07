package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.TransferenceNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.domain.vo.ValidatedTransference;
import com.xvitu.transferences.infrastructure.gateway.authorization.AuthorizationGateway;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationData;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
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
    void execute_ShouldUpdateTransferenceToSuccess_WhenAuthorizationPasses() {
        AuthorizationData authData = new AuthorizationData(true);
        when(authorizationGateway.get()).thenReturn(Mono.just(new AuthorizationResponse("success", authData)));

        ValidatedTransference validated = mock(ValidatedTransference.class);
        when(validator.validate(PAYER_ID, PAYEE_ID, AMOUNT)).thenReturn(validated);

        when(validated.payeerWallet()).thenReturn(mock(Wallet.class));
        when(validated.payeeWallet()).thenReturn(mock(Wallet.class));

        useCase.execute(command);

        verify(validator, times(1)).validate(PAYER_ID, PAYEE_ID, AMOUNT);

        // Captura transference salva
        ArgumentCaptor<Transference> transferenceCaptor = ArgumentCaptor.forClass(Transference.class);
        verify(transferenceDataProvider, times(1)).save(transferenceCaptor.capture());

        Transference saved = transferenceCaptor.getValue();
        assertEquals(pendingTransference.success().status(), saved.status());
    }

    @Test
    void execute_ShouldUpdateTransferenceToFail_WhenAuthorizationFails() {
        AuthorizationData authData = new AuthorizationData(false);
        when(authorizationGateway.get()).thenReturn(Mono.just(new AuthorizationResponse("fail", authData)));

        ValidatedTransference validated = mock(ValidatedTransference.class);
        when(validator.validate(PAYER_ID, PAYEE_ID, AMOUNT)).thenReturn(validated);
        when(validated.payeerWallet()).thenReturn(mock(Wallet.class));
        when(validated.payeeWallet()).thenReturn(mock(Wallet.class));

        useCase.execute(command);

        ArgumentCaptor<Transference> transferenceCaptor = ArgumentCaptor.forClass(Transference.class);
        verify(transferenceDataProvider, times(1)).save(transferenceCaptor.capture());

        Transference saved = transferenceCaptor.getValue();
        assertEquals(pendingTransference.fail().status(), saved.status());
    }

    @Test
    void execute_ShouldThrow_WhenTransferenceNotFound() {
        when(transferenceDataProvider.findById(TRANSFERENCE_ID)).thenReturn(Optional.empty());

        assertThrows(TransferenceNotFoundException.class, () -> useCase.execute(command));

        verify(validator, never()).validate(anyInt(), anyInt(), any());
        verify(transferenceDataProvider, never()).save(any());
    }
}
