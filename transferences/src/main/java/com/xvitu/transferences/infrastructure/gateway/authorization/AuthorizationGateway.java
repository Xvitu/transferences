package com.xvitu.transferences.infrastructure.gateway.authorization;


import com.xvitu.transferences.application.usecase.TransferUseCase;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationData;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Component
public class AuthorizationGateway {

    private final WebClient webClient;

    public AuthorizationGateway(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://util.devi.tools/api/v2").build();
    }

    // todo - documentacao
    // todo - flyway

    // se der tempo amanha
    // todo - idepotencia
    // todo - outbox

    @Retry(name = "authorizationRetry")
    @TimeLimiter(name = "authorizationTimeLimiter")
    public Mono<AuthorizationResponse> get() {
        return webClient.get()
                .uri("/authorize")
                .retrieve()
                .bodyToMono(AuthorizationResponse.class)
                .onErrorResume(WebClientResponseException.Forbidden.class, ex ->
                        Mono.just(new ObjectMapper().readValue(
                            ex.getResponseBodyAsString(),
                            AuthorizationResponse.class
                        ))
                );
    }
}
