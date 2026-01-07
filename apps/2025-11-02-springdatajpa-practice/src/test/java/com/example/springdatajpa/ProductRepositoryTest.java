package com.example.springdatajpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void testFindByPriceBetweenOrderByPriceAsc() {
        List<Product> products = productRepository.findByPriceBetweenOrderByPriceAsc(100L, 5000L);

        assertThat(products)
                .hasSize(4)
                .extracting(
                    Product::getId,
                    Product::getProductCode,
                    Product::getName,
                    Product::getPrice,
                    Product::getCategory,
                    Product::getStockQuantity,
                    Product::getCreatedAt,
                    Product::getUpdatedAt
                )
                .containsExactly(
                    tuple(3L, "P003", "オーガニックリンゴ", 200L, "食品", 100, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0)),
                    tuple(5L, "P005", "コットンTシャツ", 2500L, "衣類", 30, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0)),
                    tuple(4L, "P004", "高級オリーブオイル", 3000L, "食品", 10, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0)),
                    tuple(2L, "P002", "ワイヤレスマウス", 3500L, "家電", 50, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0))
                );
    }

    @Test
    void testFindByCategoryInAndStockQuantityGreaterThan() {
        List<String> categories = Arrays.asList("家電", "食品");
        List<Product> products = productRepository.findByCategoryInAndStockQuantityGreaterThan(categories, 10);

        assertThat(products)
                .hasSize(2)
                .extracting(
                    Product::getId,
                    Product::getProductCode,
                    Product::getName,
                    Product::getPrice,
                    Product::getCategory,
                    Product::getStockQuantity,
                    Product::getCreatedAt,
                    Product::getUpdatedAt
                )
                .containsExactly(
                    tuple(2L, "P002", "ワイヤレスマウス", 3500L, "家電", 50, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0)),
                    tuple(3L, "P003", "オーガニックリンゴ", 200L, "食品", 100, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0))
                );
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Product> products = productRepository.findByNameContainingIgnoreCase("pc");

        assertThat(products)
                .hasSize(1)
                .extracting(
                    Product::getId,
                    Product::getProductCode,
                    Product::getName,
                    Product::getPrice,
                    Product::getCategory,
                    Product::getStockQuantity,
                    Product::getCreatedAt,
                    Product::getUpdatedAt
                )
                .containsExactly(
                    tuple(1L, "P001", "高性能ノートPC", 150000L, "家電", 5, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0))
                );
    }

    @Test
    void testExistsByCategoryAndPriceGreaterThanEqual() {
        boolean result = productRepository.existsByCategoryAndPriceGreaterThanEqual("家電", 3000L);

        assertThat(result)
                .isNotNull()
                .isTrue();
    }

    @Test
    void testCountByStockQuantityLessThan() {
        Long count = productRepository.countByStockQuantityLessThan(30);

        assertThat(count)
                .isNotNull()
                .isEqualTo(3L);
    }

    @Test
    void testFirstByCategoryOrderByPriceDesc() {
        Optional<Product> product = productRepository.findFirstByCategoryOrderByPriceDesc("家電");

        assertThat(product.orElse(null))
                .isNotNull()
                .extracting(
                    Product::getId,
                    Product::getProductCode,
                    Product::getName,
                    Product::getPrice,
                    Product::getCategory,
                    Product::getStockQuantity,
                    Product::getCreatedAt,
                    Product::getUpdatedAt
                )
                .containsExactly(
                    1L, "P001", "高性能ノートPC", 150000L, "家電", 5, LocalDateTime.of(2025, 1, 1, 10, 0, 0), LocalDateTime.of(2025, 1, 1, 10, 0, 0)
                );
    }
}
