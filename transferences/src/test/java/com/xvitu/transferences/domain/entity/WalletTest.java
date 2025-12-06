package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private static final UUID WALLET_ID = UUID.randomUUID();
    private static final Integer USER_ID = 1;

    @Test
    void ensureHasFunds_ShouldNotThrowException_WhenAvailableAmountIsGreaterThanValue() {
        BigDecimal available = new BigDecimal("500.00");
        BigDecimal transferValue = new BigDecimal("100.00");
        Wallet wallet = new Wallet(WALLET_ID, USER_ID, available);

        assertDoesNotThrow(() -> wallet.ensureHasFunds(transferValue));
    }

    @Test
    void ensureHasFunds_ShouldNotThrowException_WhenAvailableAmountIsEqualToValue() {
        BigDecimal available = new BigDecimal("250.50");
        BigDecimal transferValue = new BigDecimal("250.50");
        Wallet wallet = new Wallet(WALLET_ID, USER_ID, available);

        assertDoesNotThrow(() -> wallet.ensureHasFunds(transferValue));
    }

    @Test
    void ensureHasFunds_ShouldThrowInsufficientFundsException_WhenAvailableAmountIsLessThanValue() {
        BigDecimal available = new BigDecimal("99.99");
        BigDecimal transferValue = new BigDecimal("100.00");
        Wallet wallet = new Wallet(WALLET_ID, USER_ID, available);

        assertThrows(InsufficientFundsException.class,
                () -> wallet.ensureHasFunds(transferValue)
        );
    }

    @Test
    void ensureHasFunds_ShouldNotThrowException_WhenTransferValueIsZeroAndBalancePositive() {
        BigDecimal available = new BigDecimal("10.00");
        BigDecimal transferValue = BigDecimal.ZERO;
        Wallet wallet = new Wallet(WALLET_ID, USER_ID, available);

        assertDoesNotThrow(() -> wallet.ensureHasFunds(transferValue));
    }
}