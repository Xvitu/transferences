package com.xvitu.transferences.application.command;

import java.math.BigDecimal;

public record TransferCommand(BigDecimal value, Integer payer, Integer payee) {}
