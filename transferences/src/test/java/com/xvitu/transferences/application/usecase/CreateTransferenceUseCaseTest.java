package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.CreateTransferenceCommand;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.enums.TransferenceStatus;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferencePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateTransferenceUseCaseTest {

    @Mock
    private TransferenceValidator validator;

    @Mock
    private TransferenceDataProvider transferenceDataProvider;

    @Mock
    private TransferencePublisher publisher;

    @InjectMocks
    private CreateTransferenceUseCase useCase;

    private CreateTransferenceCommand command;

    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final BigDecimal VALUE = new BigDecimal("100.00");

    @BeforeEach
    void setup() {
        command = new CreateTransferenceCommand(VALUE, PAYER_ID, PAYEE_ID);
    }

    @Test
    void shouldCreateTransferenceAndPublishEvent() {
        doNothing().when(transferenceDataProvider).save(any());

        Transference result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(VALUE, result.amount());
        assertEquals(PAYER_ID, result.payerId());
        assertEquals(PAYEE_ID, result.payeeId());
        assertEquals(TransferenceStatus.PENDING, result.status());
        assertNotNull(result.id());

        verify(validator, times(1)).validate(PAYER_ID, PAYEE_ID, VALUE);

        verify(transferenceDataProvider, times(1)).save(any(Transference.class));

        ArgumentCaptor<TransferEvent> eventCaptor = ArgumentCaptor.forClass(TransferEvent.class);
        verify(publisher, times(1)).publish(eventCaptor.capture());

        TransferEvent evt = eventCaptor.getValue();
        assertEquals(PAYER_ID, evt.payerId());
        assertEquals(PAYEE_ID, evt.payeeId());
        assertEquals(VALUE, evt.amount());
        assertEquals(result.id().toString(), evt.eventId());
    }

    @Test
    void shouldThrow_WhenValidatorFails() {
        doThrow(new UserNotFoundException(PAYER_ID))
                .when(validator)
                .validate(PAYER_ID, PAYEE_ID, VALUE);

        assertThrows(UserNotFoundException.class, () -> useCase.execute(command));

        verify(transferenceDataProvider, never()).save(any());
        verify(publisher, never()).publish(any());
    }
}