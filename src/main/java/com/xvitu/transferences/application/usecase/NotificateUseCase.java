package com.xvitu.transferences.application.usecase;


import com.xvitu.transferences.infrastructure.gateway.notification.NotificationGateway;
import com.xvitu.transferences.infrastructure.gateway.notification.request.NotificationRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificateUseCase {
    private final NotificationGateway notificationGateway;

    public NotificateUseCase(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void notify(String email, String notificationEvent) {
        notificationGateway.notificate(new NotificationRequest(email, notificationEvent));
    }
}
