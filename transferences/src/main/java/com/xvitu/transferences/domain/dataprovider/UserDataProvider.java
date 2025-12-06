package com.xvitu.transferences.domain.dataprovider;

import com.xvitu.transferences.domain.entity.User;

import java.util.Optional;

public interface UserDataProvider {
    Optional<User> findById(Integer id);
}
