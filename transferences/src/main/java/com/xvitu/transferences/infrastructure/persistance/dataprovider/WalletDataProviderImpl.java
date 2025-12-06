package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.infrastructure.persistance.adapter.WalletAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.WalletEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.WalletRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class WalletDataProviderImpl implements WalletDataProvider {

    private final WalletRepository repository;
    private final WalletAdapter adapter;


    public WalletDataProviderImpl(WalletRepository repository, WalletAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    @Override
    public Optional<Wallet> findByUserId(Integer userId) {
        Optional<WalletEntity> walletEntity = repository.findByUserId(userId);
        return walletEntity.map(adapter::toDomain);
    }
}
