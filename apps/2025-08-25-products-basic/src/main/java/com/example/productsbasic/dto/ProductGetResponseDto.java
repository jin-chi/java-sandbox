package com.example.productsbasic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.productsbasic.entity.Product;

public record ProductGetResponseDto(

        Long id,
        String name,
        BigDecimal price,
        Integer stock,
        LocalDateTime createdAt

) {
    public static ProductGetResponseDto fromEntity(Product product) {
        return new ProductGetResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedAt());
    }
}
