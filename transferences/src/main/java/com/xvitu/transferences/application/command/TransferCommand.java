package com.xvitu.transferences.application.command;

import java.math.BigDecimal;
import java.util.UUID;

// todo - payer e payee devem ser int
public record TransferCommand(BigDecimal value, UUID payer, UUID payee) {}
