package com.example.springdatajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 指定した価格の範囲内にある商品を、安い順に取得
    List<Product> findByPriceBetweenOrderByPriceAsc(Long min, Long max);

    // 指定したカテゴリーに属し、かつ在庫が一定数以上のものを取得
    List<Product> findByCategoryInAndStockQuantityGreaterThan(List<String> categories, Integer quantity);

    // 【部分一致・大文字小文字を区別しない】
    List<Product> findByNameContainingIgnoreCase(String name);

    // 【存在確認 + 複数条件】
    // 用途：特定のカテゴリの「高額商品（10万円以上）」が1つでもあるかチェック。
    // 件数を数える count よりも exists の方が高速な場合が多い。
    boolean existsByCategoryAndPriceGreaterThanEqual(String category, Long price);
    
    // 【カウント + 条件】
    // 用途：在庫が閾値を下回っている「欠品間近」の商品の数を取得（ダッシュボード用）。
    long countByStockQuantityLessThan(Integer threshold);

    // 【Top N 件の取得】
    // 用途：特定のカテゴリ内で「最も高い商品」を1つだけ取得してバナーに表示する。
    Optional<Product> findFirstByCategoryOrderByPriceDesc(String category);
}
