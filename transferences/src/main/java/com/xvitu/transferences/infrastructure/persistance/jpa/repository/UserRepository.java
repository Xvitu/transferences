package com.xvitu.transferences.infrastructure.persistance.jpa.repository;

import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> { }
