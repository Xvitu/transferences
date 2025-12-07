package com.xvitu.transferences.infrastructure.rabbitmq.publisher;

import com.xvitu.transferences.domain.enums.NotificationEventEnum;
import com.xvitu.transferences.infrastructure.rabbitmq.RabbitConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private NotificationPublisher publisher;

    @Test
    void shouldPublishNotificationEvent() {

        NotificationEvent event = new NotificationEvent(
                "user@mail.com",
                NotificationEventEnum.TRANSFERENCE_RECEIVED
        );

        publisher.publish(event);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);

        verify(rabbitTemplate, times(1))
                .convertAndSend(
                        eq(RabbitConfig.MAIN_EXCHANGE),
                        eq(RabbitConfig.NOTIFICATION_ROUTING_KEY),
                        captor.capture()
                );

        assertThat(captor.getValue())
                .isInstanceOf(NotificationEvent.class)
                .isEqualTo(event);
    }
}
