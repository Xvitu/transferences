package com.xvitu.transferences.infrastructure.persistance.jpa.repository;

import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Integer> { }
