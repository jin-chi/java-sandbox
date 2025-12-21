package com.example.springdatajpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "coupon",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_coupon_code", columnNames = {"coupon_code"})
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_code", nullable = false, length = 20)
    private String couponCode;

    @Column(name = "discount_rate", nullable = false)
    private Integer discountRate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "used_at", nullable = true)
    private LocalDateTime useAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public Coupon(String couponCode, Integer discountRate, LocalDate expiryDate, boolean isActive) {
        this.couponCode = couponCode;
        this.discountRate = discountRate;
        this.expiryDate = expiryDate;
        this.isActive = isActive;
        this.useAt = null; // 初期値は未使用
    }
}
