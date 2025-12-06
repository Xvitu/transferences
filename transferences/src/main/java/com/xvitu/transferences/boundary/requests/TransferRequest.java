package com.xvitu.transferences.boundary.requests;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(BigDecimal value, UUID payer, UUID payee) { }
