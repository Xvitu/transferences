package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import org.springframework.stereotype.Component;


@Component
public class TransferenceAdapter {
    public TransferenceEntity toEntity(Transference domainTransference) {
        return new TransferenceEntity(
                domainTransference.getId(),
                domainTransference.getAmount(),
                domainTransference.getStatus(),
                domainTransference.getPayerId(),
                domainTransference.getPayeeId()
        );
    }

    public Transference toDomain(TransferenceEntity transferenceEntity) {
        return new Transference(
                transferenceEntity.getId(),
                transferenceEntity.getAmount(),
                transferenceEntity.getStatus(),
                transferenceEntity.getPayerId(),
                transferenceEntity.getPayeeId()
        );
    }
}
