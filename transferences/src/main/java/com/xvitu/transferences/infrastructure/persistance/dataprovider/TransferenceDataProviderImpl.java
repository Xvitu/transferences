package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.dataprovider.TransferenceDataProvider;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.infrastructure.persistance.adapter.TransferenceAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.TransferenceRepository;
import org.springframework.stereotype.Component;

@Component
public class TransferenceDataProviderImpl implements TransferenceDataProvider {

    private final TransferenceRepository repository;
    private final TransferenceAdapter adapter;

    public TransferenceDataProviderImpl(TransferenceRepository repository, TransferenceAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    @Override
    public void save(Transference transference) {
        TransferenceEntity entity = adapter.toEntity(transference);
        repository.save(entity);
    }
}
