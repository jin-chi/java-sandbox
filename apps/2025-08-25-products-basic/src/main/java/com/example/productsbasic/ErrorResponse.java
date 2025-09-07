package com.example.productsbasic;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {
    private int status;
    private String message;
    private Instant timestamp;
    private List<String> errorDetails;

    public ErrorResponse(int status, String message, Instant timestamp, List<String> errorDetails) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errorDetails = errorDetails;
    }

    public ErrorResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<String> errorDetails) {
        this.errorDetails = errorDetails;
    }
}
