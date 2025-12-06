package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.enums.TransferenceStatus;
import com.xvitu.transferences.domain.exception.InvalidTransferenceException;

import java.math.BigDecimal;
import java.util.UUID;

public record Transference(UUID id, BigDecimal amount, TransferenceStatus status, Integer payerId, Integer payeeId) {

    public static Transference pending(BigDecimal amount, Integer payerId, Integer payeeId) {

        validate(amount, payerId, payeeId);

        return new Transference(
                UUID.randomUUID(),
                amount,
                TransferenceStatus.PENDING,
                payerId,
                payeeId
        );
    }

    public static void validate(BigDecimal amount, Integer payerId, Integer payeeId) {
        if (payerId.equals(payeeId)) {
            throw new InvalidTransferenceException("Payer and Payee cannot be the same");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferenceException("Amount must be greater than zero");
        }
    }
}
