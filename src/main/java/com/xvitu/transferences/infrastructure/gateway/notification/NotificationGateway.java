package com.xvitu.transferences.infrastructure.gateway.notification;

import com.xvitu.transferences.infrastructure.gateway.notification.request.NotificationRequest;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
public class NotificationGateway {

    private final WebClient webClient;

    public NotificationGateway(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://util.devi.tools/api/v1").build();
    }


    @Retry(name = "notificationRetry")
    @TimeLimiter(name = "notificationTimeLimiter")
    public CompletableFuture<Void> notificate(NotificationRequest request) {
        return webClient.post()
                .uri("/notify")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .toFuture();
    }
}
