package com.xvitu.transferences.boundary.listener;

import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;

@Component
public class TransferListener {

    private static final long MAX_RETRIES = 3L;

    @RabbitListener(queues = "transference.queue")
    public void consume(TransferEvent event, @Headers Map<String, Object> headers) {




    }

}
