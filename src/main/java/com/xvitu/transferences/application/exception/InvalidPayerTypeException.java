package com.xvitu.transferences.application.exception;

import com.xvitu.transferences.domain.enums.UserType;

public class InvalidPayerTypeException extends RuntimeException {
    public InvalidPayerTypeException(UserType type) {
        super(String.format("Payer can not be of type %s", type));
    }
}
