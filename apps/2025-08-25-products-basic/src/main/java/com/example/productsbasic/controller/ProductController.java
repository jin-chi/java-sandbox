package com.example.productsbasic.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.productsbasic.dto.ProductGetResponseDto;
import com.example.productsbasic.dto.ProductRequestDto;
import com.example.productsbasic.entity.Product;
import com.example.productsbasic.exception.ResourceNotFoundException;
import com.example.productsbasic.respository.ProductRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductRepository repo;

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<ProductGetResponseDto>> getAllProducts() {
        List<Product> products = repo.findAll();
        return ResponseEntity.ok(products.stream()
                .map(ProductGetResponseDto::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetResponseDto> getProductById(@PathVariable Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ユーザーID " + id + " が存在しません"));
        return ResponseEntity.ok(ProductGetResponseDto.fromEntity(product));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequestDto req) {
        Product createProduct = repo.save(Product.from(req));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(createProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Valid @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto req) {
        Product updateProduct = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ユーザーID: " + id + " が存在しません"));
        updateProduct.setName(req.name());
        updateProduct.setPrice(req.price());
        updateProduct.setStock(req.stock());
        repo.save(updateProduct);
        return ResponseEntity.ok(updateProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Valid @PathVariable Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("ユーザーID: " + id + " が存在しません");
    }
}
