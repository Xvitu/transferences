package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import java.math.BigDecimal;

public record TransferEvent(
        String eventId,
        Integer payerId,
        Integer payeeId,
        BigDecimal amount
) {}

