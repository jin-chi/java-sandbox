package com.example.productsbasic;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // RequestBody バリデーションエラー (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        List<String> errorDetails = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorDetails.add(error.getDefaultMessage());
        });

        errorResponse.setStatus(400);
        errorResponse.setMessage("Validation Error");
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setErrorDetails(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // RequestParam でバリデーションエラー (400)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        List<String> errorDetails = new ArrayList<>();
        ex.getConstraintViolations().forEach((error) -> {
            errorDetails.add(error.getMessage());
        });

        errorResponse.setStatus(400);
        errorResponse.setMessage("Parameter Error");
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setErrorDetails(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // JSON 解析エラー (400)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MyProblemDetail> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.BAD_REQUEST,
                detail,
                URI.create("about:blank"));
        return ResponseEntity.badRequest().body(problemDetail);
    }

    // URI Not Found (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MyProblemDetail> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.NOT_FOUND,
                detail,
                URI.create("about:blank"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    // Resource Not Found (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MyProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.NOT_FOUND,
                detail,
                URI.create("about:blank"));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    // HTTP Method Error (カスタム例外) (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MyProblemDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        String detail = ex.getMessage();
        MyProblemDetail problemDetail = MyProblemDetail.forStatusAndDetailAndType(
                HttpStatus.METHOD_NOT_ALLOWED,
                detail,
                URI.create("about:blank"));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> errorDetails = new ArrayList<>();

        String errorMessage = ex.getMessage();

        if (errorMessage.contains("Unique index or primary key violation")) {
            errorDetails.add("同じ商品名は登録できません");
        } else {
            errorDetails.add(errorMessage);
        }

        errorResponse.setStatus(409);
        errorResponse.setMessage("Conflict Error");
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setErrorDetails(errorDetails);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
