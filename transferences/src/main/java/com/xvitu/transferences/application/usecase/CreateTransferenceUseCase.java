package com.xvitu.transferences.application.usecase;

import com.xvitu.transferences.application.command.TransferCommand;
import com.xvitu.transferences.application.exception.InvalidPayerTypeException;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.application.exception.UserWalletNotFoundException;
import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

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

        User payer = userDataProvider.findById(payerId).orElseThrow(() -> new UserNotFoundException(payerId));

        userDataProvider.findById(payeeId).orElseThrow(() -> new UserNotFoundException(payeeId));

        if(payer.isShopKeeper()) {
            throw  new InvalidPayerTypeException(payer.getType());
        }

        Wallet payerWallet = walletDataProvider.findByUserId(payerId).orElseThrow(
                () -> new UserWalletNotFoundException(payerId)
        );

        walletDataProvider.findByUserId(payeeId).orElseThrow(
                () -> new UserWalletNotFoundException(payeeId)
        );

        payerWallet.ensureHasFunds(command.value());

        Transference transference = Transference.pending(command.value(), command.payer(), command.payee());
        transferenceDataProvider.save(transference);

        // todo - publica na fila
        // todo - usar outbox

        return transference;
    }
}


