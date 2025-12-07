package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.infrastructure.rabbitmq.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransferencePublisher {

    private final RabbitTemplate rabbitTemplate;

    public TransferencePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TransferEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.MAIN_EXCHANGE,
                RabbitConfig.TRANSFERENCE_ROUTING_KEY,
                event
        );
    }
}


