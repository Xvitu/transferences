package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;

public class UserAdapter {
    public UserEntity toEntity(User domainUser) {
        return new UserEntity(
                domainUser.getId(),
                domainUser.getFullName(),
                domainUser.getDocument(),
                domainUser.getEmail(),
                domainUser.getPassword()
        );
    }


    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getDocument(),
                entity.getEmail(),
                entity.getPassword()
        );
    }
}
