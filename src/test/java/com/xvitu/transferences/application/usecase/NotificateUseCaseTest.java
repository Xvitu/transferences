package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.infrastructure.gateway.notification.NotificationGateway;
import com.xvitu.transferences.infrastructure.gateway.notification.request.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificateUseCaseTest {

    private NotificationGateway notificationGateway;
    private NotificateUseCase useCase;

    @BeforeEach
    void setup() {
        notificationGateway = mock(NotificationGateway.class);
        useCase = new NotificateUseCase(notificationGateway);
    }

    @Test
    void notify_ShouldCallNotificationGatewayWithCorrectRequest() {
        String email = "user@example.com";
        String event = "TRANSFERENCE_SEND";

        ArgumentCaptor<NotificationRequest> captor = ArgumentCaptor.forClass(NotificationRequest.class);

        useCase.notify(email, event);

        verify(notificationGateway, times(1)).notificate(captor.capture());

        NotificationRequest captured = captor.getValue();

        assertEquals(email, captured.email());
        assertEquals(event, captured.template());
    }
}
