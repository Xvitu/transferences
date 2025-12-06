package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.enums.TransferenceStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class Transference {
    private final UUID id;
    private final BigDecimal amount;
    private final TransferenceStatus status;
    private final UUID payerId;
    private final UUID payeeId;

    public Transference(UUID id, BigDecimal amount, TransferenceStatus status, UUID payerId, UUID payeeId) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.payeeId = payeeId;
        this.payerId = payerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransferenceStatus getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPayeeId() {
        return payeeId;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public static Transference pending(BigDecimal amount, UUID payerId, UUID payeeId) {

        validate(amount, payerId, payeeId);

        return new Transference(
                UUID.randomUUID(),
                amount,
                TransferenceStatus.PENDING,
                payerId,
                payeeId
        );
    }

    public static void validate(BigDecimal amount, UUID payerId, UUID payeeId) {
        if (payerId.equals(payeeId)) {
            throw new IllegalArgumentException("Payer and Payee cannot be the same");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}
