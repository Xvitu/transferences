package com.xvitu.transferences.domain.dataprovider;

import com.xvitu.transferences.domain.entity.Transference;

import java.util.Optional;
import java.util.UUID;

public interface TransferenceDataProvider {
    void save(Transference transference);
    Optional<Transference> findById(UUID id);
}
