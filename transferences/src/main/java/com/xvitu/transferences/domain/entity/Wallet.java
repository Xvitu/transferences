package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {
    private final UUID id;
    private final Integer userId;
    private final BigDecimal availableAmount;

    public Wallet(UUID id, Integer userId, BigDecimal availableAmount) {
        this.id = id;
        this.userId = userId;
        this.availableAmount = availableAmount;
    }


    public UUID getId() {
        return id;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void ensureHasFunds(BigDecimal value) {
        if (this.availableAmount.compareTo(value) < 0) {
            throw new InsufficientFundsException(id);
        }
    }
}
