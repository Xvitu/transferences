package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.entity.Wallet;
import com.xvitu.transferences.infrastructure.persistance.adapter.WalletAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.WalletEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletDataProviderImplTest {

    private static final Integer USER_ID = 123;

    @Mock
    private WalletRepository repository;
    @Mock
    private WalletAdapter adapter;
    @Mock
    private WalletEntity mockEntity;
    @Mock
    private Wallet mockWallet;

    @InjectMocks
    private WalletDataProviderImpl dataProvider;

    @Test
    void findByUserId_ShouldReturnWalletInOptional_WhenEntityIsFound() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.of(mockEntity));
        when(adapter.toDomain(mockEntity)).thenReturn(mockWallet);

        Optional<Wallet> result = dataProvider.findByUserId(USER_ID);

        assertTrue(result.isPresent());
        assertEquals(mockWallet, result.get());

        verify(repository, times(1)).findByUserId(USER_ID);
        verify(adapter, times(1)).toDomain(mockEntity);
    }

    @Test
    void findByUserId_ShouldReturnEmptyOptional_WhenEntityIsNotFound() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        Optional<Wallet> result = dataProvider.findByUserId(USER_ID);

        assertFalse(result.isPresent());

        verify(repository, times(1)).findByUserId(USER_ID);
        verify(adapter, never()).toDomain(any(WalletEntity.class));
    }
}