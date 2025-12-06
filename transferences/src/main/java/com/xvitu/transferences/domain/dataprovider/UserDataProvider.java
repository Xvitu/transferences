package com.xvitu.transferences.domain.dataprovider;

import com.xvitu.transferences.domain.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserDataProvider {
    public Optional<User> findById(UUID id);
}
