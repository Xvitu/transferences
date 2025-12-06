package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CreateTransferenceUseCase {

    private final UserDataProvider userDataProvider;
    private final WalletDataProvider walletDataProvider;
    private final TransferenceDataProvider transferenceDataProvider;

    public CreateTransferenceUseCase(
            UserDataProvider userDataProvider,
            WalletDataProvider walletDataProvider,
            TransferenceDataProvider transferenceDataProvider
    ) {
        this.userDataProvider = userDataProvider;
        this.walletDataProvider = walletDataProvider;
        this.transferenceDataProvider = transferenceDataProvider;
    }

    @Transactional
    public Transference execute(TransferCommand command) {
        Integer payerId = command.payer();
        Integer payeeId = command.payee();

        // todo - criar custom exceptions
        Transference transference = Transference.pending(command.value(), command.payer(), command.payee());

        User payer = userDataProvider.findById(payerId).orElseThrow(
            () -> new RuntimeException(String.format("Payer %s not found!", payerId))
        );

        userDataProvider.findById(payeeId).orElseThrow(
                () -> new RuntimeException(String.format("Payee %s not found!", payeeId))
        );

        if(payer.isShopKeeper()) {
            throw  new RuntimeException("Payer user can not be of shopkeeper type");
        }

        Wallet payerWallet = walletDataProvider.findByUserId(payerId).orElseThrow(
                () -> new RuntimeException(String.format("Payer %s has no wallet!", payerId))
        );

        walletDataProvider.findByUserId(payeeId).orElseThrow(
                () -> new RuntimeException(String.format("Payee %s has no wallet!", payeeId))
        );

        payerWallet.ensureHasFunds(command.value());

        transferenceDataProvider.save(transference);

        // todo - publica na fila
        // todo - usar outbox

        return transference;
    }
}


