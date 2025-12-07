package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.enums.TransferenceStatus;
import com.xvitu.transferences.domain.exception.InvalidTransferenceException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferenceTest {

    private static final Integer PAYER_ID = 10;
    private static final Integer PAYEE_ID = 20;
    private static final BigDecimal POSITIVE_AMOUNT = new BigDecimal("150.75");

    @Test
    void pending_ShouldCreatePendingTransference_WhenDataIsValid() {
        Transference transference = Transference.pending(POSITIVE_AMOUNT, PAYER_ID, PAYEE_ID);

        assertNotNull(transference);
        assertNotNull(transference.id());
        assertEquals(POSITIVE_AMOUNT, transference.amount());
        assertEquals(PAYER_ID, transference.payerId());
        assertEquals(PAYEE_ID, transference.payeeId());
        assertEquals(TransferenceStatus.PENDING, transference.status());
    }

    @Test
    void validate_ShouldThrowException_WhenPayerAndPayeeAreTheSame() {
        Integer sameId = 30;

        InvalidTransferenceException exception = assertThrows(InvalidTransferenceException.class, () ->
                Transference.pending(POSITIVE_AMOUNT, sameId, sameId)
        );

        assertEquals("Payer and Payee cannot be the same", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenAmountIsZero() {
        BigDecimal zeroAmount = BigDecimal.ZERO;

        InvalidTransferenceException exception = assertThrows(InvalidTransferenceException.class, () ->
                Transference.pending(zeroAmount, PAYER_ID, PAYEE_ID)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());
    }

    @Test
    void validate_ShouldThrowException_WhenAmountIsNegative() {
        BigDecimal negativeAmount = new BigDecimal("-10.00");

        InvalidTransferenceException exception = assertThrows(InvalidTransferenceException.class, () ->
                Transference.pending(negativeAmount, PAYER_ID, PAYEE_ID)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());
    }
}