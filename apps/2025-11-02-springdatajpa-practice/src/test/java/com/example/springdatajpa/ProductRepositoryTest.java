package com.example.springdatajpa;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springdatajpa.entity.Product;
import com.example.springdatajpa.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryTest {
    
    @Autowired
    ProductRepository productRepository;

    @Test
    void testFindByPriceBetweenOrderByPriceAsc(Long min, Long max) {
        List<Product> products = productRepository.findByPriceBetweenOrderByPriceAsc(100L, 1000L);

        assertThat(products)
                .hasSize(2)
                .extracting(
                    Product::getId,
                    Product::getName,
                    Product::getPrice,
                    Product::getCategory,
                    Product::getStockQuantity
                )
                .containsExactly(
                    tuple(1L, "name", 1000, "category", 10)
                );
    }
}
