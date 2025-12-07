package com.xvitu.transferences.infrastructure.persistance.jpa.repository;

import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransferenceRepository extends JpaRepository<TransferenceEntity, UUID> { }
