package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.domain.enums.NotificationEventEnum;

public record NotificationEvent(String email, NotificationEventEnum event) {}
