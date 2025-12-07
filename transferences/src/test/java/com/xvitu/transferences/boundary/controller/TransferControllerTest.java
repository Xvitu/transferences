package com.xvitu.transferences.boundary.controller;

import com.xvitu.transferences.application.command.CreateTransferenceCommand;
import com.xvitu.transferences.application.usecase.CreateTransferenceUseCase;
import com.xvitu.transferences.boundary.controller.requests.TransferRequest;
import com.xvitu.transferences.domain.entity.Transference;
import com.xvitu.transferences.domain.enums.TransferenceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateTransferenceUseCase createTransferenceUseCase;

    private static final Integer PAYER_ID = 1;
    private static final Integer PAYEE_ID = 2;
    private static final BigDecimal TRANSFER_VALUE = new BigDecimal("100.0");
    private static final String API_URL = "/transfer";

    @Test
    void transfer_ShouldReturn201CreatedAndTransference_WhenRequestIsValid() throws Exception {
        TransferRequest request = new TransferRequest(TRANSFER_VALUE, PAYER_ID, PAYEE_ID);

        Transference mockTransference = new Transference(
                UUID.randomUUID(),
                TRANSFER_VALUE,
                TransferenceStatus.PENDING,
                PAYER_ID,
                PAYEE_ID
        );

        when(createTransferenceUseCase.execute(any(CreateTransferenceCommand.class)))
                .thenReturn(mockTransference);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(mockTransference.id().toString()))
                .andExpect(jsonPath("$.amount").value(TRANSFER_VALUE))
                .andExpect(jsonPath("$.payerId").value(PAYER_ID))
                .andExpect(jsonPath("$.payeeId").value(PAYEE_ID))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(createTransferenceUseCase, times(1)).execute(any(CreateTransferenceCommand.class));
    }

    @Test
    void transfer_ShouldReturn400BadRequest_WhenValidationFails() throws Exception {
        TransferRequest invalidRequest = new TransferRequest(null, PAYER_ID, PAYEE_ID);

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("O valor da transferência é obrigatório."));

        verify(createTransferenceUseCase, never()).execute(any(CreateTransferenceCommand.class));
    }
}