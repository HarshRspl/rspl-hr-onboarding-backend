package com.rspl.onboarding.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex) {
        ex.printStackTrace(); // prints full trace to Railway logs
        return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error",   ex.getClass().getSimpleName(),
                "message", ex.getMessage() != null ? ex.getMessage() : "Unknown error"
        ));
    }
}

