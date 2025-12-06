package com.xvitu.transferences.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {
    private final UUID id;
    private final UUID userId;
    private final BigDecimal availableAmount;

    public Wallet(UUID id, UUID userId, BigDecimal availableAmount) {
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

    public UUID getUserId() {
        return userId;
    }

    public void ensureHasFunds(BigDecimal value) {
        if (this.availableAmount.compareTo(value) < 0) {
            // todo - exception
            throw new RuntimeException();
        }
    }
}
