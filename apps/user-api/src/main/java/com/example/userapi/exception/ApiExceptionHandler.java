package com.example.userapi.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

  /* レスポンス用DTO */
  record ApiError(
    LocalDateTime ts,
    int status,
    String message
  ) {}

  /* 400: DTOバリデーション */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> badReqeust(MethodArgumentNotValidException ex) {
    String msg = ex.getBindingResult()
                   .getFieldErrors().stream()
                   .map(e -> e.getDefaultMessage())
                   .collect(Collectors.joining(", "));
    return build(HttpStatus.BAD_REQUEST, msg);
  }

  /* 400: @RequestParamなど */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiError> badReqeust(ConstraintViolationException ex) {
    String msg = ex.getConstraintViolations().stream()
                   .map(v -> v.getMessage())
                   .collect(Collectors.joining(", "));
    return build(HttpStatus.BAD_REQUEST, msg);
  }

  /* 401: 認証失敗（login） */
  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<ApiError> unauthorized(SecurityException ex) {
    return build(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  /* 401: 業務データみ存在 */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> notFound(IllegalArgumentException ex) {
    return build(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  /* 共通ビルダー */
  public ResponseEntity<ApiError> build(HttpStatus status, String message) {
    return ResponseEntity.status(status)
            .body(new ApiError(LocalDateTime.now(), status.value(), message));
  }
}
