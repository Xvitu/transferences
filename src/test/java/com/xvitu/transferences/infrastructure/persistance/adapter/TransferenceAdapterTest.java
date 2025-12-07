package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.enums.TransferenceStatus;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransferenceAdapterTest {

    private final TransferenceAdapter adapter = new TransferenceAdapter();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();

        Transference domain = new Transference(
                id,
                new BigDecimal("50.00"),
                TransferenceStatus.PROCESSING,
                1,
                2
        );

        TransferenceEntity entity = adapter.toEntity(domain);

        assertEquals(domain.id(), entity.getId());
        assertEquals(domain.amount(), entity.getAmount());
        assertEquals(domain.status(), entity.getStatus());
        assertEquals(domain.payerId(), entity.getPayerId());
        assertEquals(domain.payeeId(), entity.getPayeeId());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();

        TransferenceEntity entity = new TransferenceEntity(
                id,
                new BigDecimal("99.90"),
                TransferenceStatus.SUCCESS,
                3,
               5
        );

        Transference domain = adapter.toDomain(entity);

        assertEquals(entity.getId(), domain.id());
        assertEquals(entity.getAmount(), domain.amount());
        assertEquals(entity.getStatus(), domain.status());
        assertEquals(entity.getPayerId(), domain.payerId());
        assertEquals(entity.getPayeeId(), domain.payeeId());
    }
}
