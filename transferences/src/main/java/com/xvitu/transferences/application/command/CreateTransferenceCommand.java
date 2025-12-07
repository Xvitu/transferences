package com.xvitu.transferences.application.command;

import java.math.BigDecimal;

public record CreateTransferenceCommand(BigDecimal value, Integer payer, Integer payee) {}
