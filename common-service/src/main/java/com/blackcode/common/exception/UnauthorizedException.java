package com.blackcode.common.exception;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }

    // Konstruktor dengan pesan dan cause
    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
