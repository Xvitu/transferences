package com.xvitu.transferences.infrastructure.persistance.jpa.repository;

import com.xvitu.transferences.infrastructure.persistance.jpa.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByUserId(UUID userId);
}
