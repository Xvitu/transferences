package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.infrastructure.persistance.adapter.UserAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserDataProviderImpl  implements UserDataProvider {
    private final UserRepository repository;
    private final UserAdapter adapter;

    public UserDataProviderImpl(UserRepository repository, UserAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        return userEntity.map(adapter::toDomain);
    }
}
