package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.dataprovider.WalletDataProvider;
import com.xvitu.transferences.infrastructure.persistance.adapter.WalletAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.WalletRepository;

public class WalletDataProviderImpl implements WalletDataProvider {

    private final WalletRepository repository;
    private final WalletAdapter adapter;


    public WalletDataProviderImpl(WalletRepository repository, WalletAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }
}
