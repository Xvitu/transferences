package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(UUID id, Integer userId, BigDecimal availableAmount) {

    public void ensureHasFunds(BigDecimal value) {
        if (this.availableAmount.compareTo(value) < 0) {
            throw new InsufficientFundsException(id);
        }
    }
}
