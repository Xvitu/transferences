package com.xvitu.transferences.application.command;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferCommand(UUID transferenceId, Integer payerId, Integer payeeId, BigDecimal amount) { }
