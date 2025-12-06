package com.xvitu.transferences.infrastructure.persistance.dataprovider;

import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.infrastructure.persistance.adapter.TransferenceAdapter;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.TransferenceEntity;
import com.xvitu.transferences.infrastructure.persistance.jpa.repository.TransferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void save_ShouldConvertAndSaveTransferenceSuccessfully() {
        when(adapter.toEntity(mockTransference)).thenReturn(mockEntity);
        when(repository.save(mockEntity)).thenReturn(mockEntity);

        dataProvider.save(mockTransference);

        verify(adapter, times(1)).toEntity(mockTransference);
        verify(repository, times(1)).save(mockEntity);
    }
}