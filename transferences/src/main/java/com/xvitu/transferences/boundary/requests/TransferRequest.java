package com.xvitu.transferences.boundary.requests;

import java.math.BigDecimal;

public record TransferRequest(BigDecimal value, Integer payer, Integer payee) { }
