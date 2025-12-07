package com.xvitu.transferences.domain.service;

import com.xvitu.transferences.application.exception.InvalidPayerTypeException;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.application.exception.UserWalletNotFoundException;
import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.domain.vo.ValidatedTransference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferenceValidator {
    private final UserDataProvider userDataProvider;
    private final WalletDataProvider walletDataProvider;

    public TransferenceValidator(
            UserDataProvider userDataProvider,
            WalletDataProvider walletDataProvider
    ) {
        this.userDataProvider = userDataProvider;
        this.walletDataProvider = walletDataProvider;
    }

    public ValidatedTransference validate(Integer payerId, Integer payeeId, BigDecimal amount) {
        User payer = userDataProvider.findById(payerId).orElseThrow(() -> new UserNotFoundException(payerId));

        userDataProvider.findById(payeeId).orElseThrow(() -> new UserNotFoundException(payeeId));

        if(payer.isShopKeeper()) {
            throw  new InvalidPayerTypeException(payer.getType());
        }

        Wallet payerWallet = walletDataProvider.findByUserId(payerId).orElseThrow(
                () -> new UserWalletNotFoundException(payerId)
        );

        Wallet payeeWallet = walletDataProvider.findByUserId(payeeId).orElseThrow(
                () -> new UserWalletNotFoundException(payeeId)
        );

        payerWallet.ensureHasFunds(amount);

        return new ValidatedTransference(payerWallet, payeeWallet);
    }

}
