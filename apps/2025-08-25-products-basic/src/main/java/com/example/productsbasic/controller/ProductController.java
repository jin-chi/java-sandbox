package com.example.productsbasic.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.example.productsbasic.dto.ProductCreateUpdateResponseDto;
import com.example.productsbasic.dto.ProductGetResponseDto;
import com.example.productsbasic.dto.ProductRequestDto;
import com.example.productsbasic.entity.Product;
import com.example.productsbasic.exception.ResourceNotFoundException;
import com.example.productsbasic.mapper.ProductMapper;
import com.example.productsbasic.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService service;
    private ProductMapper mapper;

    public ProductController(ProductService service, ProductMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProductGetResponseDto>> getAllProducts() {
        logger.info("商品情報全権取得");
        return ResponseEntity.ok(service.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetResponseDto> getProductById(@PathVariable Long id) {
        logger.info("商品情報取得 [id:" + id + "]");
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<ProductCreateUpdateResponseDto> createProduct(@Valid @RequestBody ProductRequestDto req) {
        logger.info("商品情報登録 [request: " + req + "]");
        Product createdProduct = service.createProduct(req);
        ProductCreateUpdateResponseDto responseDto = mapper.toCreateUpdateResponseDto(createdProduct);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCreateUpdateResponseDto> updateProduct(@Valid @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto req) {
        logger.info("商品情報更新 [request: " + req + "]");
        return ResponseEntity.ok(service.updateProduct(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Valid @PathVariable Long id) {
        logger.info("商品情報削除 [id: " + id + "]");
        if (service.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        throw new ResourceNotFoundException("IDが存在しません [id: " + id + "]");
    }
}
