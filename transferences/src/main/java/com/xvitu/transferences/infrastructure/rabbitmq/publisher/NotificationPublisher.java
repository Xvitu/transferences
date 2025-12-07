package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.infrastructure.rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {
    private final RabbitTemplate rabbitTemplate;

    public NotificationPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(NotificationEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIN_EXCHANGE,
                RabbitConfig.NOTIFICATION_ROUTING_KEY,
                event
        );
    }

}
