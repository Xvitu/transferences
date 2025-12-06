package com.xvitu.transferences.domain.dataprovider;

import com.xvitu.transferences.domain.entity.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletDataProvider {
    public Optional<Wallet> findByUserId(Integer userId);
}
