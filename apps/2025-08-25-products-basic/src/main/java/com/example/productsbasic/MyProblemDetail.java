package com.example.productsbasic;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.ProblemDetail;

public class MyProblemDetail extends ProblemDetail {
    private final Instant timestamp;

    public MyProblemDetail(int status, String detail, URI type) {
        super();
        this.setStatus(status);
        this.setDetail(detail);
        this.setType(type);
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public static MyProblemDetail forStatusAndDetailAndType(int status, String detail, URI type) {
        return new MyProblemDetail(status, detail, type);
    }
}
