package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.domain.enums.NotificationEventEnum;

import java.util.UUID;

public record NotificationEvent(String email, String event, String eventId) {
    public NotificationEvent(String email, NotificationEventEnum event) {
        this(email, event.name(), UUID.randomUUID().toString());
    }
}
