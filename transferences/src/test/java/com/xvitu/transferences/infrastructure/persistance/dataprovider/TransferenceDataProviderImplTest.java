package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.infrastructure.persistance.adapter.TransferenceAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.TransferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferenceDataProviderImplTest {

    @Mock
    private TransferenceRepository repository;
    @Mock
    private TransferenceAdapter adapter;
    @Mock
    private Transference mockTransference;
    @Mock
    private TransferenceEntity mockEntity;

    @InjectMocks
    private TransferenceDataProviderImpl dataProvider;

    private UUID transferenceId;
    private TransferenceEntity entity;
    private Transference domain;


    @BeforeEach
    void setUp() {
        transferenceId = UUID.randomUUID();
        entity = new TransferenceEntity();
        domain = Transference.pending(BigDecimal.TEN, 1, 2);
    }

    @Test
    void save_ShouldConvertAndSaveTransferenceSuccessfully() {
        when(adapter.toEntity(mockTransference)).thenReturn(mockEntity);
        when(repository.save(mockEntity)).thenReturn(mockEntity);

        dataProvider.save(mockTransference);

        verify(adapter, times(1)).toEntity(mockTransference);
        verify(repository, times(1)).save(mockEntity);
    }

    @Test
    void findById_ShouldReturnTransference_WhenEntityExists() {
        // Mock repository e adapter
        when(repository.findById(transferenceId)).thenReturn(Optional.of(entity));
        when(adapter.toDomain(entity)).thenReturn(domain);

        Optional<Transference> result = dataProvider.findById(transferenceId);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());

        verify(repository, times(1)).findById(transferenceId);
        verify(adapter, times(1)).toDomain(entity);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenEntityDoesNotExist() {
        when(repository.findById(transferenceId)).thenReturn(Optional.empty());

        Optional<Transference> result = dataProvider.findById(transferenceId);

        assertFalse(result.isPresent());

        verify(repository, times(1)).findById(transferenceId);
        verify(adapter, never()).toDomain(any());
    }
}