package com.xvitu.transferences.domain.exception;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID walletId) {
        super(String.format("Wallet %s has insufficient funds to perform transference", walletId));
    }
}
