package com.xvitu.transferences.domain.exception;

import java.util.UUID;

public class InvalidTransferenceException extends RuntimeException {
    public InvalidTransferenceException(String message) {
        super(message);
    }
}
