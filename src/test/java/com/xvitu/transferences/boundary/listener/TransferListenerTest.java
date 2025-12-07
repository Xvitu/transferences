package com.xvitu.transferences.boundary.listener;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.usecase.TransferUseCase;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferListenerTest {

    @Mock
    private TransferUseCase transferUseCase;
    @InjectMocks
    private TransferListener listener;

    @Test
    void shouldConsumeTransferEventAndExecuteUseCase() {
        String eventId = UUID.randomUUID().toString();
        Integer payerId = 10;
        Integer payeeId = 20;
        BigDecimal amount = BigDecimal.valueOf(150.50);

        TransferEvent event = new TransferEvent(eventId, payerId, payeeId, amount);

        listener.consume(event);

        ArgumentCaptor<TransferCommand> captor = ArgumentCaptor.forClass(TransferCommand.class);
        verify(transferUseCase, times(1)).execute(captor.capture());

        TransferCommand captured = captor.getValue();

        assertThat(captured).isNotNull();
        assertThat(captured.transferenceId()).isEqualTo(UUID.fromString(eventId));
        assertThat(captured.payerId()).isEqualTo(payerId);
        assertThat(captured.payeeId()).isEqualTo(payeeId);
        assertThat(captured.amount()).isEqualByComparingTo(amount);
    }
}
