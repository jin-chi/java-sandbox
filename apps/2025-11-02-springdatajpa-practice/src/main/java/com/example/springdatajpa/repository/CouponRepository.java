package com.example.springdatajpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
    // 指定した日付よりも期限が先で、かつ有効なクーポンを取得
    void findByExpiryDateAfterAndIsActiveTrue(LocalDate date);

    // また使われ愛知内（Null）クーポンを、期限が近い順に取得
    void findByUsedAtIsNullOrderByExpiryDateAsc();

    // 検証用：コード、有効フラグ、期限チェック
    Optional<Coupon> findByCouponCodeAndIsActiveTrueAndExpiryDateAfter(String code, LocalDate date);

    // 上位3件取得
    List<Coupon> findTop3ByIsActiveTrueOrderByDiscountRateDesc();

    // 期限切れかつ未使用
    List<Coupon> findByExpiryDateBeforeAndUsedAtIsNull(LocalDate date);

    // 特定コード以外かつ有効
    List<Coupon> findByCouponCodeNotAndIsActiveTrue(String code);
}
