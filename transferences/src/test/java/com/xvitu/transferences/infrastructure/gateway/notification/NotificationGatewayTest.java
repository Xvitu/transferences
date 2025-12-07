package com.xvitu.transferences.infrastructure.gateway.notification;

import com.xvitu.transferences.infrastructure.gateway.notification.request.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationGatewayTest {

    private ExchangeFunction exchangeFunction;
    private NotificationGateway gateway;

    @BeforeEach
    void setup() {
        exchangeFunction = mock(ExchangeFunction.class);

        WebClient.Builder builder = WebClient.builder()
                .exchangeFunction(exchangeFunction);

        gateway = new NotificationGateway(builder);
    }

    @Test
    void notificate_ShouldCompleteSuccessfully_OnHttp200() throws ExecutionException, InterruptedException {
        NotificationRequest request = new NotificationRequest("user@example.com", "TRANSFERENCE_SEND");

        ClientResponse clientResponse = mock(ClientResponse.class);

        when(clientResponse.statusCode()).thenReturn(HttpStatus.OK);
        when(clientResponse.bodyToMono(Void.class)).thenReturn(Mono.empty());

        when(exchangeFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.just(clientResponse));

        CompletableFuture<Void> future = gateway.notificate(request);

        future.get();

        verify(exchangeFunction, times(1)).exchange(any(ClientRequest.class));
    }

    @Test
    void notificate_ShouldFail_WhenServiceReturnsError() {
        NotificationRequest request = new NotificationRequest("user@example.com", "TRANSFERENCE_SEND");

        when(exchangeFunction.exchange(any(ClientRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("service unavailable")));

        CompletableFuture<Void> future = gateway.notificate(request);

        try {
            future.get();
        } catch (Exception e) {
            assert e.getCause().getMessage().equals("service unavailable");
        }

        verify(exchangeFunction, times(1)).exchange(any(ClientRequest.class));
    }
}
