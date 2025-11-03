package com.scrapsail.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Global exception handler for all unhandled exceptions.
 * Returns structured JSON error responses with request ID for tracing.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        String requestId = UUID.randomUUID().toString();
        
        logger.error("Unhandled exception [requestId={}, path={}]: {}", 
            requestId, request.getDescription(false), ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal server error");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("code", "INTERNAL_ERROR");
        errorResponse.put("requestId", requestId);
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("path", request.getDescription(false).replace("uri=", ""));

        // Include stack trace in development, exclude in production
        String env = System.getenv("SPRING_PROFILES_ACTIVE");
        if (env == null || !env.equals("prod")) {
            errorResponse.put("stackTrace", ex.getStackTrace());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        
        String requestId = UUID.randomUUID().toString();
        
        logger.warn("Illegal argument [requestId={}]: {}", requestId, ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid request");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("code", "BAD_REQUEST");
        errorResponse.put("requestId", requestId);
        errorResponse.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        String requestId = UUID.randomUUID().toString();
        
        logger.error("Runtime exception [requestId={}]: {}", requestId, ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Runtime error");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("code", "RUNTIME_ERROR");
        errorResponse.put("requestId", requestId);
        errorResponse.put("timestamp", Instant.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

