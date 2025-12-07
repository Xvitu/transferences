package com.xvitu.transferences.boundary.controller;

import com.xvitu.transferences.application.exception.InvalidPayerTypeException;
import com.xvitu.transferences.application.exception.UserNotFoundException;
import com.xvitu.transferences.application.exception.UserWalletNotFoundException;
import com.xvitu.transferences.domain.exception.InsufficientFundsException;
import com.xvitu.transferences.domain.exception.InvalidTransferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private record ErrorResponse(String message, String errorType) {}

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(getErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserNotFoundException.class, UserWalletNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ObjectError firstError = ex.getBindingResult().getAllErrors().getFirst();
        String errorMessage = firstError.getDefaultMessage();

        return new ResponseEntity<>(new ErrorResponse(errorMessage, ex.getClass().getSimpleName()), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler({InvalidPayerTypeException.class, InsufficientFundsException.class, InvalidTransferenceException.class})
    public ResponseEntity<ErrorResponse> handleUnprocessableContentExceptions(Exception ex) {
        return new ResponseEntity<>(getErrorResponse(ex), HttpStatus.UNPROCESSABLE_CONTENT);
    }

    private ErrorResponse getErrorResponse(Exception ex) {
       return new ErrorResponse(ex.getMessage(), ex.getClass().getSimpleName());
    }
}