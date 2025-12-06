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
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        TransferenceEntity entity = adapter.toEntity(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getAmount(), entity.getAmount());
        assertEquals(domain.getStatus(), entity.getStatus());
        assertEquals(domain.getPayerId(), entity.getPayerId());
        assertEquals(domain.getPayeeId(), entity.getPayeeId());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();

        TransferenceEntity entity = new TransferenceEntity(
                id,
                new BigDecimal("99.90"),
                TransferenceStatus.SUCCESS,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        Transference domain = adapter.toDomain(entity);

        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getAmount(), domain.getAmount());
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getPayerId(), domain.getPayerId());
        assertEquals(entity.getPayeeId(), domain.getPayeeId());
    }
}
