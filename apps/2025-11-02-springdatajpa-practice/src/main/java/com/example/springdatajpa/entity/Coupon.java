package com.example.springdatajpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.springdatajpa.entity.base.BaseAuditEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "coupon",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_coupon_code", columnNames = {"coupon_code"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon extends BaseAuditEntity {
    
    @Column(name = "coupon_code", nullable = false, length = 20)
    private String couponCode;

    @Column(name = "discount_rate", nullable = false)
    private Integer discountRate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "used_at", nullable = true)
    private LocalDateTime usedAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * クーポンを使用済みにする
     * @param useDate 使用日時
     */
    public void use(LocalDateTime useDate) {
        if (!this.isActive) {
            throw new IllegalStateException("無効なクーポンは使用できません");
        }
        if (this.usedAt != null) {
            throw new IllegalStateException("既に使用済みのクーポンです");
        }
        if (useDate.toLocalDate().isAfter(this.expiryDate)) {
            throw new IllegalStateException("期限切れのクーポンです");
        }

        this.usedAt = useDate;
        // 使用したら無効にする用件であれば isActive = false; をここに入れる
    }

    /**
     * クーポンを無効化する（管理画面などから操作）
     */
    public void deactivate() {
        this.isActive = false;
    }
}
