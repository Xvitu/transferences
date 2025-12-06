package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.infrastructure.persistance.adapter.UserAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataProviderImplTest {

    private static final Integer USER_ID = 100;

    @Mock
    private UserRepository repository;
    @Mock
    private UserAdapter adapter;
    @Mock
    private UserEntity mockEntity;
    @Mock
    private User mockUser;

    @InjectMocks
    private UserDataProviderImpl dataProvider;

    @Test
    void findById_ShouldReturnUserInOptional_WhenEntityIsFound() {
        when(repository.findById(USER_ID)).thenReturn(Optional.of(mockEntity));
        when(adapter.toDomain(mockEntity)).thenReturn(mockUser);

        Optional<User> result = dataProvider.findById(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        verify(repository, times(1)).findById(USER_ID);
        verify(adapter, times(1)).toDomain(mockEntity);
    }

    @Test
    void findById_ShouldReturnEmptyOptional_WhenEntityIsNotFound() {
        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<User> result = dataProvider.findById(USER_ID);

        assertFalse(result.isPresent());

        verify(repository, times(1)).findById(USER_ID);
        verify(adapter, never()).toDomain(any(UserEntity.class));
    }
}