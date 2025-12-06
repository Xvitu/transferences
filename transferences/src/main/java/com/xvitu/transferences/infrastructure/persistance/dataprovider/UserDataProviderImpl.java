package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.dataprovider.UserDataProvider;
import com.xvitu.transferences.infrastructure.persistance.adapter.UserAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.UserRepository;

public class UserDataProviderImpl  implements UserDataProvider {

    private final UserRepository repository;
    private final UserAdapter adapter;


    public UserDataProviderImpl(UserRepository repository, UserAdapter adapter) {
        this.repository = repository;
        this.adapter = adapter;
    }
}
