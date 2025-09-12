package com.example.productsbasic;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.Nullable;

public class MyProblemDetail extends ProblemDetail {

    private final Instant timestamp;
    @Nullable
    private List<Map<String, String>> errors;

    public MyProblemDetail(HttpStatus status, String detail, URI type, URI instance) {
        super();
        this.setStatus(status.value());
        this.setDetail(detail);
        this.setType(type);
        this.setInstance(instance);
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public List<Map<String, String>> getErrors() {
        return this.errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = new ArrayList<>(errors);
    }

    public static MyProblemDetail forStatusAndDetailAndType(HttpStatus status, String detail, URI type,
            URI instance) {
        return new MyProblemDetail(status, detail, type, instance);
    }
}
