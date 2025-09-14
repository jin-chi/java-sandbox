package com.example.productsbasic.dto;

import java.math.BigDecimal;

public record ProductCreateUpdateResponseDto(
        String name,
        BigDecimal price,
        Integer stock
) {}
