package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.infrastructure.rabbitmq.RabbitConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferencePublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TransferencePublisher transferencePublisher;

    private static final UUID TRANSFERENCE_ID = UUID.randomUUID();
    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final BigDecimal AMOUNT = new BigDecimal("50.00");

    private final TransferEvent mockEvent = new TransferEvent(
            TRANSFERENCE_ID.toString(),
            PAYER_ID,
            PAYEE_ID,
            AMOUNT
    );

    @Test
    void publish_ShouldCallConvertAndSendWithCorrectArguments() {
        transferencePublisher.publish(mockEvent);

        verify(rabbitTemplate, times(1)).convertAndSend(
                RabbitConfig.MAIN_EXCHANGE,
                RabbitConfig.MAIN_ROUTING_KEY,
                mockEvent
        );

        verifyNoMoreInteractions(rabbitTemplate);
    }
}