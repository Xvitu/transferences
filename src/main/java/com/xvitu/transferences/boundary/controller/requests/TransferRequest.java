package com.xvitu.transferences.boundary.controller.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "O valor da transferência é obrigatório.")
        @Min(value = 1, message = "O valor deve ser maior ou igual a R$ 1,00.")
        BigDecimal value,

        @NotNull(message = "O payer id é obrigatório.")
        Integer payer,

        @NotNull(message = "O payee id é obrigatório.")
        Integer payee
) { }
