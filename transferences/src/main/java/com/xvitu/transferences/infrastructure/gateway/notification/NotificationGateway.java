package com.xvitu.transferences.infrastructure.gateway.notification;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NotificationGateway {

    private final WebClient webClient;

    public NotificationGateway(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://util.devi.tools/api/v1").build();
    }


    @Retry(name = "notificationRetry")
    @TimeLimiter(name = "notificationTimeLimiter")
    public Mono<Void> notificate() {
        return webClient.post()
                .uri("/notify")
                .exchangeToMono(ClientResponse::releaseBody);
    }
}
