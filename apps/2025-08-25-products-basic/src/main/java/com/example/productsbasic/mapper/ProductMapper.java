package com.example.productsbasic.mapper;

import org.springframework.stereotype.Component;

import com.example.productsbasic.dto.ProductCreateUpdateResponseDto;
import com.example.productsbasic.dto.ProductGetResponseDto;
import com.example.productsbasic.dto.ProductRequestDto;
import com.example.productsbasic.entity.Product;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto req) {
        return new Product(req.name(), req.price(), req.stock());
    }

    public ProductGetResponseDto toGetResponseDto(Product product) {
        return new ProductGetResponseDto(product.getId(), product.getName(), product.getPrice(), product.getStock(),
                product.getCreatedAt());
    }

    public ProductCreateUpdateResponseDto toCreateUpdateResponseDto(Product product) {
        return new ProductCreateUpdateResponseDto(product.getName(), product.getPrice(), product.getStock());
    }
}
