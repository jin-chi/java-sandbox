package com.example.springdatajpa.entity;

import com.example.springdatajpa.entity.base.BaseAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(
    name = "product",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_code", columnNames = {"product_code"})
    }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Product extends BaseAuditEntity {

    @Column(name = "product_code", nullable = false, length = 20)
    @ToString.Include
    private String productCode;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
}
