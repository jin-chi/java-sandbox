package com.example.productsbasic.exception;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // RequestBody バリデーションエラー (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            WebRequest request) {

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map((error) -> Map.of(
                        "field", error.getField(),
                        "defaultMessage", error.getDefaultMessage()))
                .toList();

        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.BAD_REQUEST,
                "Validation failed to request body.",
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));
        problemDetail.setErrors(errors);

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    // RequestParam でバリデーションエラー (400)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MyProblemDetail> handleConstraintViolationException(ConstraintViolationException ex,
            WebRequest request) {

        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map((error) -> {
                    Map<String, String> fieldMap = new LinkedHashMap<>();
                    fieldMap.put("field", error.getPropertyPath().toString());
                    fieldMap.put("message", error.getMessage());
                    return fieldMap;
                }).collect(Collectors.toList());

        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.BAD_REQUEST,
                "Validation failed to request URI.",
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));
        problemDetail.setErrors(errors);

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    // JSON(request body) 解析エラー (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MyProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
            WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.BAD_REQUEST,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    // Path の型違いエラー (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MyProblemDetail> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.BAD_REQUEST,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.badRequest().body(problemDetail);
    }

    // URI Not Found (404)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MyProblemDetail> handleNoResourceFoundException(NoResourceFoundException ex,
            WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.NOT_FOUND,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    // Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MyProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.NOT_FOUND,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    // HTTP Method Error (カスタム例外) (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MyProblemDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.METHOD_NOT_ALLOWED,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);
    }

    // データ重複エラー (409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<MyProblemDetail> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        List<Map<String, String>> errorDetails = new ArrayList<>();
        String errorMessage = ex.getMostSpecificCause().toString();

        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.CONFLICT,
                errorMessage,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        if (errorMessage.contains("Unique index or primary key violation")) {
            errorDetails.add(Map.of("errorMessage", "同じ商品名は登録できません"));
            problemDetail.setErrors(errorDetails);
        }

        logger.warn("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    // その他エラー
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyProblemDetail> handleOtherException(Exception ex, WebRequest request) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.INTERNAL_SERVER_ERROR,
                detail,
                URI.create("about:blank"),
                URI.create(request.getDescription(false).substring(4)));

        problemDetail.setProperty("exceptionName", ex.getClass());

        logger.error("errMsg={}, resDetail={}", ex, problemDetail);

        return ResponseEntity.internalServerError().body(problemDetail);
    }
}
