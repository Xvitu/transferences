package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.WalletEntity;
import org.springframework.stereotype.Component;

@Component
public class WalletAdapter {
    public WalletEntity toEntity(Wallet domainWallet) {
        return new WalletEntity(
                domainWallet.getId(),
                domainWallet.getAvailableAmount(),
                domainWallet.getUserId()
        );
    }

    public Wallet toDomain(WalletEntity entity) {
        return new Wallet(
                entity.getId(),
                entity.getUserId(),
                entity.getAvailableAmount()
        );
    }
}
