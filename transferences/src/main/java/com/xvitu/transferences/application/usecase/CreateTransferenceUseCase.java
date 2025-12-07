package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.CreateTransferenceCommand;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferEvent;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.TransferencePublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateTransferenceUseCase {

    private final TransferenceValidator transferenceValidator;
    private final TransferenceDataProvider transferenceDataProvider;
    private final TransferencePublisher transferencePublisher;

    public CreateTransferenceUseCase(
            TransferenceValidator transferenceValidator,
            TransferenceDataProvider transferenceDataProvider,
            TransferencePublisher transferencePublisher
    ) {
        this.transferenceValidator = transferenceValidator;
        this.transferenceDataProvider = transferenceDataProvider;
        this.transferencePublisher = transferencePublisher;
    }

    @Transactional
    public Transference execute(CreateTransferenceCommand command) {
        Integer payerId = command.payer();
        Integer payeeId = command.payee();

        transferenceValidator.validate(payerId, payeeId, command.value());

        Transference transference = Transference.pending(command.value(), command.payer(), command.payee());
        transferenceDataProvider.save(transference);

        publishEvent(command, transference.id());

        // todo - usar outbox

        return transference;
    }

    private void publishEvent(CreateTransferenceCommand command, UUID transferenceId) {
        transferencePublisher.publish(
                new TransferEvent(
                        transferenceId.toString(), command.payer(), command.payee(), command.value()
                )
        );
    }
}


