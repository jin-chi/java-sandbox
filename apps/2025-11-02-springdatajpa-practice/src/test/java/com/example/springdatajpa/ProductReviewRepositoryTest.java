package com.example.springdatajpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springdatajpa.entity.ProductReview;
import com.example.springdatajpa.repository.ProductReviewRepository;

@DataJpaTest
public class ProductReviewRepositoryTest {
    
    @Autowired
    ProductReviewRepository productReviewRepository;

    @Test
    void testFindByRatingGreaterThanEqualAndIsApprovedTrueOrderByReviewDataDesc() {
        List<ProductReview> reviews = productReviewRepository.findByRatingGreaterThanEqualAndIsApprovedTrueOrderByReviewDateDesc(5);
        assertThat(reviews)
                .hasSize(2)
                .extracting(
                    ProductReview::getId,
                    productReview -> productReview.getCustomer().getId(),
                    ProductReview::getProductName,
                    ProductReview::getRating,
                    ProductReview::getContent,
                    ProductReview::getReviewDate,
                    ProductReview::isApproved
                )
                .containsExactly(
                    tuple(5L, 2L, "イヤホンZ", 5, "音質が素晴らしい。", LocalDate.of(2024, 10, 20), true),
                    tuple(1L, 1L, "スマホX", 5, "最高傑作。", LocalDate.of(2024, 10, 1), true)
                );
    }

    @Test
    void testFindByCustomer_Id() {
        List<ProductReview> reviews = productReviewRepository.findByCustomer_Id(1L);
        assertThat(reviews)
                .hasSize(2)
                .extracting(
                    ProductReview::getId,
                    productReview -> productReview.getCustomer().getId(),
                    ProductReview::getProductName,
                    ProductReview::getRating,
                    ProductReview::getContent,
                    ProductReview::getReviewDate,
                    ProductReview::isApproved
                )
                .containsExactly(
                    tuple(1L, 1L, "スマホX", 5, "最高傑作。", LocalDate.of(2024, 10, 1), true),
                    tuple(3L, 1L, "スマホX", 1, "動作が遅い。", LocalDate.of(2024, 10, 10), false)
                );
    }

    @Test
    void testCountByProductName() {
        Long count = productReviewRepository.countByProductName("スマホX");
        assertThat(count).isEqualTo(3);
    }

    @Test
    void testExistsByCustomer_IdAndProductName() {
        boolean isExists = productReviewRepository.existsByCustomer_IdAndProductName(2L, "PC-Lite");
        assertThat(isExists).isTrue();
    }

    @Test
    void testFindByOrderByRatingDescCustomer_IdAsc() {
        List<ProductReview> reviews = productReviewRepository.findByOrderByRatingDescIdAsc();
        assertThat(reviews)
                .hasSize(5)
                .extracting(
                    ProductReview::getId,
                    productReview -> productReview.getCustomer().getId(),
                    ProductReview::getProductName,
                    ProductReview::getRating,
                    ProductReview::getContent,
                    ProductReview::getReviewDate,
                    ProductReview::isApproved
                )
                .containsExactly(
                    tuple(1L, 1L, "スマホX", 5, "最高傑作。", LocalDate.of(2024, 10, 1), true),
                    tuple(5L, 2L, "イヤホンZ", 5, "音質が素晴らしい。", LocalDate.of(2024, 10, 20), true),
                    tuple(4L, 3L, "スマホX", 4, "満足です。", LocalDate.of(2024, 10, 15), true),
                    tuple(2L, 2L, "PC-Lite", 3, "普通に使える。", LocalDate.of(2024, 10, 5), true),
                    tuple(3L, 1L, "スマホX", 1, "動作が遅い。", LocalDate.of(2024, 10, 10), false)
                );
    }
}
