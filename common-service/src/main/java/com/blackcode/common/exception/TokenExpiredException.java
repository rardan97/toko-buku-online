package com.blackcode.common.exception;

public class TokenExpiredException extends RuntimeException{
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
