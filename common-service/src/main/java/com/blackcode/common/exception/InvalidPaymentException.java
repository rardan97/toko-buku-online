package com.blackcode.common.exception;

public class InvalidPaymentException extends RuntimeException{
    public InvalidPaymentException(String message) {
        super(message);
    }
}
