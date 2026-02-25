package com.rspl.onboarding.api;

import com.rspl.onboarding.service.OnboardingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(OnboardingService.NotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> notFound(OnboardingService.NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.fail(ex.getMessage(), null));
  }

  @ExceptionHandler(OnboardingService.ValidationException.class)
  public ResponseEntity<ApiResponse<Object>> badRequest(OnboardingService.ValidationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail("VALIDATION_ERROR", List.of(ex.getMessage())));
  }

  @ExceptionHandler(OnboardingService.ConflictException.class)
  public ResponseEntity<ApiResponse<Object>> conflict(OnboardingService.ConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiResponse.fail(ex.getMessage(), ex.getDetails()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> beanValidation(MethodArgumentNotValidException ex) {
    var errs = ex.getBindingResult().getFieldErrors().stream()
        .map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.fail("VALIDATION_ERROR", errs));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> generic(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.fail("INTERNAL_ERROR", ex.getMessage()));
  }
}
