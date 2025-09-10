package com.example.productsbasic;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class MyProblemDetail extends ProblemDetail {
    private final Instant timestamp;

    public MyProblemDetail(HttpStatus status, String detail, URI type) {
        super();
        this.setStatus(status.value());
        this.setDetail(detail);
        this.setType(type);
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public static MyProblemDetail forStatusAndDetailAndType(HttpStatus status, String detail, URI type) {
        return new MyProblemDetail(status, detail, type);
    }
}
