package com.example.productsbasic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        List<String> errorDetail = new ArrayList<>();
        errorDetail.add(ex.getMessage());

        errorResponse.setStatus(404);
        errorResponse.setMessage("Not Found Elements");
        errorResponse.setTimestamp(Instant.now());
        errorResponse.setErrorDetails(errorDetail);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
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
