package com.xvitu.transferences.application.exception;

import java.util.UUID;

public class TransferenceNotFoundException extends RuntimeException {
    public TransferenceNotFoundException(UUID transferenceId) {
        super(String.format("Transference %s not found", transferenceId.toString()));
    }
}
