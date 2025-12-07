package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.CreateTransferenceCommand;
import com.xvitu.transferences.application.exception.InvalidPayerTypeException;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.application.exception.UserWalletNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.enums.TransferenceStatus;
import com.xvitu.transferences.domain.enums.UserType;
import com.xvitu.transferences.domain.exception.InsufficientFundsException;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferencePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransferenceUseCaseTest {

    @Mock
    private UserDataProvider userDataProvider;
    @Mock
    private WalletDataProvider walletDataProvider;
    @Mock
    private TransferenceDataProvider transferenceDataProvider;
    @Mock
    private Wallet payerWallet;
    @Mock
    private TransferencePublisher publisher;

    @InjectMocks
    private CreateTransferenceUseCase createTransferenceUseCase;

    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final BigDecimal TRANSFER_VALUE = new BigDecimal("100.00");
    private CreateTransferenceCommand validCommand;
    private User customerPayer;
    private User shopkeeperPayee;

    @BeforeEach
    void setUp() {
        validCommand = new CreateTransferenceCommand(TRANSFER_VALUE, PAYER_ID, PAYEE_ID);

        customerPayer = new User(PAYER_ID, "Payer User", "11122233344", "payer@test.com", "pass", UserType.CUSTOMER);
        shopkeeperPayee = new User(PAYEE_ID, "Payee User", "55566677788", "payee@test.com", "pass", UserType.SHOPKEEPER);
    }

    @Test
    void execute_ShouldCreateAndSavePendingTransference_WhenPayerIsCustomerAndHasFunds() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.of(new Wallet(UUID.randomUUID(), PAYEE_ID, new BigDecimal("0.00"))));

        ArgumentCaptor<TransferEvent> transferEventArgumentCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        doNothing().when(publisher).publish(transferEventArgumentCaptor.capture());

        doNothing().when(payerWallet).ensureHasFunds(TRANSFER_VALUE);

        doNothing().when(transferenceDataProvider).save(any(Transference.class));

        Transference result = createTransferenceUseCase.execute(validCommand);

        assertNotNull(result);
        assertEquals(TRANSFER_VALUE, result.amount());
        assertEquals(PAYER_ID, result.payerId());
        assertEquals(PAYEE_ID, result.payeeId());
        assertEquals(TransferenceStatus.PENDING, result.status());

        TransferEvent capturedEvent = transferEventArgumentCaptor.getValue();
        assertEquals(PAYEE_ID, capturedEvent.payeeId());
        assertEquals(PAYER_ID, capturedEvent.payerId());
        assertEquals(TRANSFER_VALUE, capturedEvent.amount());

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYEE_ID);
        verify(payerWallet, times(1)).ensureHasFunds(TRANSFER_VALUE);
        verify(transferenceDataProvider, times(1)).save(any(Transference.class));
        verify(publisher, times(1)).publish(any(TransferEvent.class));
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenPayerDoesNotExist() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, never()).findById(PAYEE_ID);
        verify(walletDataProvider, never()).findByUserId(anyInt());
        verify(transferenceDataProvider, never()).save(any());
    }

    @Test
    void execute_ShouldThrowUserNotFoundException_WhenPayeeDoesNotExist() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));

        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, never()).findByUserId(anyInt());
        verify(transferenceDataProvider, never()).save(any());
    }

    @Test
    void execute_ShouldThrowInvalidPayerTypeException_WhenPayerIsShopKeeper() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        User shopKeeperPayer = new User(PAYER_ID, "Shop Keeper", "99988877766", "shop@test.com", "pass", UserType.SHOPKEEPER);
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(shopKeeperPayer));

        InvalidPayerTypeException exception = assertThrows(InvalidPayerTypeException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        assertEquals("Payer can not be of type SHOPKEEPER", exception.getMessage());

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, never()).findByUserId(anyInt());
        verify(transferenceDataProvider, never()).save(any());
    }

    @Test
    void execute_ShouldThrowUserWalletNotFoundException_WhenPayerWalletDoesNotExist() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.empty());

        assertThrows(UserWalletNotFoundException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, never()).findByUserId(PAYEE_ID);
        verify(transferenceDataProvider, never()).save(any());
    }

    @Test
    void execute_ShouldThrowUserWalletNotFoundException_WhenPayeeWalletDoesNotExist() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.empty());

        assertThrows(UserWalletNotFoundException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYEE_ID);
        verify(transferenceDataProvider, never()).save(any());
    }


    @Test
    void execute_ShouldThrowInsufficientFundsException_WhenPayerHasNoFunds() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.of(new Wallet(UUID.randomUUID(), PAYEE_ID, new BigDecimal("0.00"))));

        doThrow(new InsufficientFundsException(payerWallet.id())).when(payerWallet).ensureHasFunds(TRANSFER_VALUE);

        assertThrows(InsufficientFundsException.class, () ->
                createTransferenceUseCase.execute(validCommand)
        );

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYEE_ID);
        verify(payerWallet, times(1)).ensureHasFunds(TRANSFER_VALUE);
        verify(transferenceDataProvider, never()).save(any());
    }
}