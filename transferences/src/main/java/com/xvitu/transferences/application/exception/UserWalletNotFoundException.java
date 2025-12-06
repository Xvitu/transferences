package com.xvitu.transferences.application.exception;

public class UserWalletNotFoundException extends RuntimeException {
    public UserWalletNotFoundException(Integer userId) {
        super(String.format("User %s wallet not found", userId));
    }
}
