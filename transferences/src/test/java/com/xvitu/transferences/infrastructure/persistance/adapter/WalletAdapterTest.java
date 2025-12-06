package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.WalletEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class WalletAdapterTest {

    private final WalletAdapter adapter = new WalletAdapter();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("150.00");

        Wallet domain = new Wallet(id, userId, amount);

        WalletEntity entity = adapter.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals(userId, entity.getUserId());
        assertEquals(amount, entity.getAvailableAmount());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("200.00");

        WalletEntity entity = new WalletEntity(id, amount, userId);

        Wallet domain = adapter.toDomain(entity);

        assertEquals(id, domain.getId());
        assertEquals(userId, domain.getUserId());
        assertEquals(amount, domain.getAvailableAmount());
    }
}
