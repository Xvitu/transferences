package com.xvitu.transferences.boundary.listener;

import com.xvitu.transferences.application.usecase.NotificateUseCase;
import com.xvitu.transferences.infrastructure.rabbitmq.RabbitConfig;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class NotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    private final NotificateUseCase notificateUseCase;

    public NotificationListener(NotificateUseCase notificateUseCase) {
        this.notificateUseCase = notificateUseCase;
    }

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE)
    public void consume(NotificationEvent event) {
        logger.info("Received new message in notification queue {}", event.eventId());
        this.notificateUseCase.notify(event.email(), event.event());
        logger.info("Notification sent for message {}", event.eventId());

    }
}
