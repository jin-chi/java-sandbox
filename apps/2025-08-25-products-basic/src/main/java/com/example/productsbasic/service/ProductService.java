package com.example.productsbasic.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.productsbasic.dto.ProductCreateUpdateResponseDto;
import com.example.productsbasic.dto.ProductGetResponseDto;
import com.example.productsbasic.dto.ProductRequestDto;
import com.example.productsbasic.entity.Product;
import com.example.productsbasic.exception.ResourceNotFoundException;
import com.example.productsbasic.mapper.ProductMapper;
import com.example.productsbasic.respository.ProductRepository;

@Service
public class ProductService {

    private ProductRepository repo;
    private ProductMapper mapper;

    public ProductService(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<ProductGetResponseDto> getAllProducts() {
        List<Product> products = repo.findAll();
        return products.stream().map(mapper::toGetResponseDto).collect(Collectors.toList());
    }

    public ProductGetResponseDto getProductById(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ユーザーID " + id + " が存在しません"));
        return mapper.toGetResponseDto(product);
    }

    public Product createProduct(ProductRequestDto req) {
        return repo.save(mapper.toEntity(req));
    }

    public ProductCreateUpdateResponseDto updateProduct(Long id, ProductRequestDto req) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ユーザーID: " + id + " が存在しません"));
        product.setName(req.name());
        product.setPrice(req.price());
        product.setStock(req.stock());
        return mapper.toCreateUpdateResponseDto(repo.save(product));
    }

    public boolean deleteProduct(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
