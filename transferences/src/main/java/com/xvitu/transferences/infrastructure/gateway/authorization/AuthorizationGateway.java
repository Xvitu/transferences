package com.xvitu.transferences.infrastructure.gateway.authorization;


import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthorizationGateway {

    private final WebClient webClient;

    public AuthorizationGateway(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://util.devi.tools/api/v2").build();
    }

    @Retry(name = "authorizationRetry")
    @TimeLimiter(name = "authorizationTimeLimiter")
    public Mono<AuthorizationResponse> get() {
        return webClient.get()
                .uri("/authorize")
                .retrieve()
                .bodyToMono(AuthorizationResponse.class);
    }
}
