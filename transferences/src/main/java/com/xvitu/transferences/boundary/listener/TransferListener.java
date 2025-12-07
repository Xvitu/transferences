package com.xvitu.transferences.boundary.listener;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.usecase.TransferUseCase;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.Map;
import java.util.UUID;

@Component
public class TransferListener {
    private static final Logger logger = LoggerFactory.getLogger(TransferListener.class);

    public final TransferUseCase transferUseCase;

    public TransferListener(TransferUseCase transferUseCase) {
        this.transferUseCase = transferUseCase;
    }


    @RabbitListener(queues = "transference.queue")
    public void consume(TransferEvent event) {
        logger.info("Received new message {}", event.eventId());

        TransferCommand command = new TransferCommand(
                UUID.fromString(event.eventId()), event.payerId(), event.payeeId(), event.amount()
        );

        transferUseCase.execute(command);
    }

}
