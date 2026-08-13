package com.example.product_search;

public record ProductResponseDto(
    String name,
    String category,
    Integer price,
    Integer stock
) {
    public static ProductResponseDto from(Product product) {
        return new ProductResponseDto(
            product.getName(),
            product.getCategory(),
            product.getPrice(),
            product.getStock()
        );
    }
}
