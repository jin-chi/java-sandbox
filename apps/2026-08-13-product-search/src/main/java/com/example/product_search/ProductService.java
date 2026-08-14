package com.example.product_search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDto> search(ProductRequestDto req) {
        if (req.isEmpty())
            return List.of();

        Specification<Product> spec = ProductSpecifications.nameContains(req.name())
                .and(ProductSpecifications.categoryEquals(req.category()))
                .and(ProductSpecifications.priceFrom(req.priceFrom()))
                .and(ProductSpecifications.priceTo(req.priceTo()))
                .and(ProductSpecifications.inStock(req.inStock()));

        List<ProductResponseDto> result = productRepository.findAll(spec).stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new ProductNotFoundException("Product not found");
        }

        return result;
    }
}
