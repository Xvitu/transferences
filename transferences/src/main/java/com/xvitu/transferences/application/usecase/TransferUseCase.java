package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.TransferenceNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.enums.NotificationEventEnum;
import com.xvitu.transferences.domain.service.TransferenceValidator;
import com.xvitu.transferences.domain.vo.ValidatedTransference;
import com.xvitu.transferences.infrastructure.gateway.authorization.AuthorizationGateway;
import com.xvitu.transferences.infrastructure.gateway.authorization.response.AuthorizationResponse;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationEvent;
import com.xvitu.transferences.infrastructure.rabbitmq.publisher.NotificationPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TransferUseCase {
    private final TransferenceValidator transferenceValidator;
    private final TransferenceDataProvider transferenceDataProvider;
    private final AuthorizationGateway authorizationGateway;
    private final WalletDataProvider walletDataProvider;
    private final NotificationPublisher notificationPublisher;

    public TransferUseCase(
            TransferenceDataProvider transferenceDataProvider,
            AuthorizationGateway authorizationGateway,
            TransferenceValidator transferenceValidator,
            WalletDataProvider walletDataProvider, NotificationPublisher notificationPublisher
    ) {
        this.transferenceValidator = transferenceValidator;
        this.transferenceDataProvider = transferenceDataProvider;
        this.authorizationGateway = authorizationGateway;
        this.walletDataProvider = walletDataProvider;
        this.notificationPublisher = notificationPublisher;
    }

    @Transactional
    public void execute(TransferCommand command) {
        Integer payerId = command.payerId();
        Integer payeeId = command.payeeId();

        Transference transference = transferenceDataProvider.findById(command.transferenceId())
                .orElseThrow(() -> new TransferenceNotFoundException(command.transferenceId()));

        ValidatedTransference validated = transferenceValidator.validate(
                payerId, payeeId, command.amount()
        );

        AuthorizationResponse response = authorizationGateway.get().block();

        Wallet updatedPayer = validated.payeerWallet().withdraw(transference.amount());
        walletDataProvider.save(updatedPayer);

        Wallet updatedPayee = validated.payeeWallet().deposit(transference.amount());
        walletDataProvider.save(updatedPayee);

        Transference updated = response.data().authorization()
                ? transference.success()
                : transference.fail();

        transferenceDataProvider.save(updated);

        notifyUser(updated, validated.payer(), validated.payee());
    }

    private void notifyUser(Transference transference, User payer, User payee) {
        ArrayList<NotificationEvent> notificationEvents = new ArrayList<>();
        switch (transference.status()){
            case FAILED:
                notificationEvents.add(new NotificationEvent(payer.getEmail(), NotificationEventEnum.TRANSFERENCE_FAILED));
                break;
            case SUCCESS:
                notificationEvents.add(new NotificationEvent(payer.getEmail(), NotificationEventEnum.TRANSFERENCE_SEND));
                notificationEvents.add(new NotificationEvent(payee.getEmail(), NotificationEventEnum.TRANSFERENCE_RECEIVED));
                break;
            default:
                break;
        }

        notificationEvents.forEach(notificationPublisher::publish);
    }
}