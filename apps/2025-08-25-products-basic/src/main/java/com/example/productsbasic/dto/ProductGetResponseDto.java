package com.example.productsbasic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductGetResponseDto(

        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        LocalDateTime createdAt

) {}
