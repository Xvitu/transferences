package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.TransferenceNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.infrastructure.gateway.authorization.AuthorizationGateway;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransferUseCase {
    private final TransferenceValidator transferenceValidator;
    private final TransferenceDataProvider transferenceDataProvider;
    private final AuthorizationGateway authorizationGateway;

    public TransferUseCase(
            TransferenceDataProvider transferenceDataProvider,
            AuthorizationGateway authorizationGateway,
            TransferenceValidator transferenceValidator
    ) {
        this.transferenceValidator = transferenceValidator;
        this.transferenceDataProvider = transferenceDataProvider;
        this.authorizationGateway = authorizationGateway;
    }

    public void execute(TransferCommand command) {
        Integer payerId = command.payerId();
        Integer payeeId = command.payeeId();

        Transference transference = transferenceDataProvider.findById(command.transferenceId())
                .orElseThrow( () -> new TransferenceNotFoundException(command.transferenceId()));

        transferenceValidator.validate(payerId, payeeId, command.amount());

        authorizationGateway.get().flatMap( response -> {
            Transference updatedTransference = response.data().authorization()
                    ? transference.success()
                    : transference.fail();

            return Mono.fromRunnable(() -> transferenceDataProvider.save(updatedTransference))
                    .thenReturn(updatedTransference);
        }).subscribe();
    }
}
