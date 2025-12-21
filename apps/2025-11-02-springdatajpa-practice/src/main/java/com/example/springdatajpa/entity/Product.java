package com.example.springdatajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "product",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_code", columnNames = {"product_code"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) // Primary Key は Setter を生成しない
    private long id;

    @Column(name = "product_code", nullable = false, length = 20)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String category;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    public Product(String productCode, String name, Long price, String category, Integer stockQuantity) {
        this.productCode = productCode;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stockQuantity = stockQuantity;
    }
}
