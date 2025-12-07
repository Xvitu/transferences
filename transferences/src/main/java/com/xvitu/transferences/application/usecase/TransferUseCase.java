package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.TransferenceNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.domain.vo.ValidatedTransference;
import com.xvitu.transferences.infrastructure.gateway.authorization.AuthorizationGateway;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TransferUseCase {
    private final TransferenceValidator transferenceValidator;
    private final TransferenceDataProvider transferenceDataProvider;
    private final AuthorizationGateway authorizationGateway;
    private final WalletDataProvider walletDataProvider;

    public TransferUseCase(
            TransferenceDataProvider transferenceDataProvider,
            AuthorizationGateway authorizationGateway,
            TransferenceValidator transferenceValidator,
            WalletDataProvider walletDataProvider
    ) {
        this.transferenceValidator = transferenceValidator;
        this.transferenceDataProvider = transferenceDataProvider;
        this.authorizationGateway = authorizationGateway;
        this.walletDataProvider = walletDataProvider;
    }

    @Transactional
    public void execute(TransferCommand command) {
        Integer payerId = command.payerId();
        Integer payeeId = command.payeeId();

        Transference transference = transferenceDataProvider.findById(command.transferenceId())
                .orElseThrow( () -> new TransferenceNotFoundException(command.transferenceId()));

        ValidatedTransference validatedTransference = transferenceValidator.validate(payerId, payeeId, command.amount());

        authorizationGateway.get().flatMap( response -> {
            Wallet updatedPayerWallet = validatedTransference.payeerWallet().withdraw(transference.amount());
            walletDataProvider.save(updatedPayerWallet);

            Wallet updatedPayeeWallet = validatedTransference.payeeWallet().deposit(transference.amount());
            walletDataProvider.save(updatedPayeeWallet);

            Transference updatedTransference = response.data().authorization()
                    ? transference.success()
                    : transference.fail();

            return Mono.fromRunnable(() -> transferenceDataProvider.save(updatedTransference))
                    .thenReturn(updatedTransference);
        }).subscribe();
    }
}
