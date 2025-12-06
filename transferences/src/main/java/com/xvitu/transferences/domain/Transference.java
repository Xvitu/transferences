package com.xvitu.transferences.domain;

import com.xvitu.transferences.domain.enums.TransferenceStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class Transference {
    private UUID id;
    private BigDecimal amount;
    private TransferenceStatus status;
    private UUID originUserId;
    private UUID destinationUserId;

}
