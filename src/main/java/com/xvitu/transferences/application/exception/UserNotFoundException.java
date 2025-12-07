package com.xvitu.transferences.application.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Integer userId) {
        super(String.format("User %s not found", userId));
    }
}
