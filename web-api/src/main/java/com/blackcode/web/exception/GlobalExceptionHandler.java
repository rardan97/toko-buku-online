package com.blackcode.web.exception;


import com.blackcode.common.exception.*;
import com.blackcode.common.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataNotFound(DataNotFoundException ex, HttpServletRequest req) {
        logger.warn("Data not found at {}: {}", req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest req) {
        logger.warn("Duplicate resource at {}: {}", req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPaymentException(InvalidPaymentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest req) {
        logger.warn("Email already exists at {}: {}", req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({InvalidJwtException.class, TokenExpiredException.class, UnauthorizedException.class})
    public ResponseEntity<ApiResponse<Object>> handleJwtAndUnauthorized(RuntimeException ex, HttpServletRequest req) {
        logger.warn("Unauthorized access at {}: {}", req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ApiResponse<Object>> handleTokenRefresh(TokenRefreshException ex, HttpServletRequest req) {
        logger.warn("Token refresh error at {}: {}", req.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN,
                "Token: " + ex.getToken() + ", Message: " + ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        logger.warn("Invalid credentials at {}", req.getRequestURI());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccountExpired(AccountExpiredException ex, HttpServletRequest req) {
        logger.warn("Account expired at {}", req.getRequestURI());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Account has expired");
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ApiResponse<Object>> handleLocked(LockedException ex, HttpServletRequest req) {
        logger.warn("Account locked at {}", req.getRequestURI());
        return buildResponse(HttpStatus.FORBIDDEN, "Account is locked");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleDisabled(DisabledException ex, HttpServletRequest req) {
        logger.warn("Account disabled at {}", req.getRequestURI());
        return buildResponse(HttpStatus.FORBIDDEN, "Account is disabled");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        logger.warn("Validation error at {}: {}", req.getRequestURI(), errors);
        return buildResponse(HttpStatus.BAD_REQUEST, errors);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest req) {

        logger.warn("Illegal argument at {}: {}", req.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAll(Exception ex, HttpServletRequest req) {
        logger.error("Unexpected error at {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    private ResponseEntity<ApiResponse<Object>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(message, status.value()));
    }


}
