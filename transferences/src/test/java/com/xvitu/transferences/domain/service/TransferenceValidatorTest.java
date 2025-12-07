package com.xvitu.transferences.domain.service;

import com.xvitu.transferences.application.exception.InvalidPayerTypeException;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.application.exception.UserWalletNotFoundException;
import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.enums.UserType;
import com.xvitu.transferences.domain.exception.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenceValidatorTest {

    @Mock
    private UserDataProvider userDataProvider;

    @Mock
    private WalletDataProvider walletDataProvider;

    @Mock
    private Wallet payerWallet;

    private TransferenceValidator validator;

    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final BigDecimal AMOUNT = new BigDecimal("100.00");

    private User customerPayer;
    private User shopkeeperPayee;

    @BeforeEach
    void setup() {
        validator = new TransferenceValidator(userDataProvider, walletDataProvider);

        customerPayer = new User(PAYER_ID, "Payer", "11122233344", "payer@test.com", "pass", UserType.CUSTOMER);
        shopkeeperPayee = new User(PAYEE_ID, "Payee", "55566677788", "payee@test.com", "pass", UserType.SHOPKEEPER);
    }

    @Test
    void validate_ShouldPass_WhenAllValidationsPass() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));
        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.of(new Wallet(UUID.randomUUID(), PAYEE_ID, BigDecimal.ZERO)));

        doNothing().when(payerWallet).ensureHasFunds(AMOUNT);

        assertDoesNotThrow(() -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYEE_ID);
        verify(payerWallet, times(1)).ensureHasFunds(AMOUNT);
    }

    @Test
    void validate_ShouldThrowUserNotFoundException_WhenPayerNotFound() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, never()).findById(PAYEE_ID);
        verify(walletDataProvider, never()).findByUserId(anyInt());
    }

    @Test
    void validate_ShouldThrowUserNotFoundException_WhenPayeeNotFound() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(userDataProvider, times(1)).findById(PAYER_ID);
        verify(userDataProvider, times(1)).findById(PAYEE_ID);
        verify(walletDataProvider, never()).findByUserId(anyInt());
    }

    @Test
    void validate_ShouldThrowInvalidPayerTypeException_WhenPayerIsShopkeeper() {
        User shopKeeperPayer = new User(PAYER_ID, "Shop", "99988877766", "shop@test.com", "pass", UserType.SHOPKEEPER);
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(shopKeeperPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));

        assertThrows(InvalidPayerTypeException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(walletDataProvider, never()).findByUserId(anyInt());
    }

    @Test
    void validate_ShouldThrowUserWalletNotFoundException_WhenPayerWalletNotFound() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));
        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.empty());

        assertThrows(UserWalletNotFoundException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, never()).findByUserId(PAYEE_ID);
    }

    @Test
    void validate_ShouldThrowUserWalletNotFoundException_WhenPayeeWalletNotFound() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));
        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.empty());

        assertThrows(UserWalletNotFoundException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(walletDataProvider, times(1)).findByUserId(PAYER_ID);
        verify(walletDataProvider, times(1)).findByUserId(PAYEE_ID);
    }

    @Test
    void validate_ShouldThrowInsufficientFundsException_WhenPayerHasNoFunds() {
        when(userDataProvider.findById(PAYER_ID)).thenReturn(Optional.of(customerPayer));
        when(userDataProvider.findById(PAYEE_ID)).thenReturn(Optional.of(shopkeeperPayee));
        when(walletDataProvider.findByUserId(PAYER_ID)).thenReturn(Optional.of(payerWallet));
        when(walletDataProvider.findByUserId(PAYEE_ID)).thenReturn(Optional.of(new Wallet(UUID.randomUUID(), PAYEE_ID, BigDecimal.ZERO)));

        doThrow(new InsufficientFundsException(payerWallet.id()))
                .when(payerWallet).ensureHasFunds(AMOUNT);

        assertThrows(InsufficientFundsException.class, () -> validator.validate(PAYER_ID, PAYEE_ID, AMOUNT));

        verify(payerWallet, times(1)).ensureHasFunds(AMOUNT);
    }
}
