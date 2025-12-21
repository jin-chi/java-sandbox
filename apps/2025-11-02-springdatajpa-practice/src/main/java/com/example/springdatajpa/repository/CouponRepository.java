package com.example.springdatajpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    
    // 指定した日付よりも期限が先で、かつ有効なクーポンを取得
    void findByExpiryDateAndIsActiveTrue(LocalDate date);

    // また使われ愛知内（Null）クーポンを、期限が近い順に取得
    void findByUseAtIsNullOrderByExpiryDateAsc();

    // 【検証用クエリ】
    // 用途：チェックアウト時に、コードが有効で、かつ期限が切れていないか一発で確認。
    Optional<Coupon> findByCouponCodeAndIsActiveTrueAndExpiryDateAfter(String code, LocalDate date);

    // 【特定のリスト件数制限】
    // 用途：キャンペーン一覧などで「割引率が高い順にトップ3件」を表示。
    List<Coupon> findTop3ByIsActiveTrueOrderByDiscountRateDesc();

    // 【Null 判定の組み合わせ】
    // 用途：期限は切れているが「未使用」のクーポンを抽出。
    // 未使用のまま執行したデータの分析に使用。
    List<Coupon> findByExpiryDateBeforeAndUsedAtIsNull(LocalDate date);

    // 【複数条件の否定】
    // 用途：特定のコード以外の有効なクーポンを全て取得（競合回避など）。
    List<Coupon> findByCouponCodeNotAndIsActiveTrue(String code);
}
