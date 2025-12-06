package com.xvitu.transferences.domain.dataprovider;

import com.xvitu.transferences.domain.entity.Wallet;

import java.util.Optional;

public interface WalletDataProvider {
    Optional<Wallet> findByUserId(Integer userId);
}
