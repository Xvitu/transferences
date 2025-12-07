package com.xvitu.transferences.infrastructure.gateway.authorization;

import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationData;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthorizationGatewayTest {

    private ExchangeFunction exchangeFunction;
    private AuthorizationGateway gateway;

    @BeforeEach
    void setup() {
        exchangeFunction = mock(ExchangeFunction.class);

        WebClient webClient = WebClient.builder()
                .exchangeFunction(exchangeFunction)
                .build();

        gateway = new AuthorizationGateway(webClient);
    }

    @Test
    void get_ShouldReturnAuthorizationResponse_WhenServiceSucceeds() {
        AuthorizationResponse responseBody = new AuthorizationResponse("success", new AuthorizationData(true));

        ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
        when(clientResponse.bodyToMono(AuthorizationResponse.class)).thenReturn(Mono.just(responseBody));

        when(exchangeFunction.exchange(any(ClientRequest.class))).thenReturn(Mono.just(clientResponse));

        Mono<AuthorizationResponse> result = gateway.get();

        StepVerifier.create(result)
                .expectNextMatches(resp -> resp.data().authorization())
                .verifyComplete();
    }
}
