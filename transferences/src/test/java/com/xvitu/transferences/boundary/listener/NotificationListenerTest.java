package com.xvitu.transferences.boundary.listener;

import com.xvitu.transferences.application.usecase.NotificateUseCase;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NotificationListenerTest {

    private NotificateUseCase notificateUseCase;
    private NotificationListener listener;

    @BeforeEach
    void setup() {
        notificateUseCase = mock(NotificateUseCase.class);
        listener = new NotificationListener(notificateUseCase);
    }

    @Test
    void consume_ShouldCallNotificateUseCase() {
        NotificationEvent event = new NotificationEvent(
                "user@example.com",
                "TRANSFERENCE_SEND",
                "1234-5678"
        );

        listener.consume(event);

        verify(notificateUseCase, times(1))
                .notify("user@example.com", "TRANSFERENCE_SEND");
    }
}
