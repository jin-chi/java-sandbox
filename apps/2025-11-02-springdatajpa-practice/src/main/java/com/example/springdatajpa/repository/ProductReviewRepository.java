package com.example.springdatajpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.ProductReview;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    
    // 1. レーティングと承認フラグによる AND 検索・ソート
    // 評価が指定値以上かつ承認済みのレビューを、日付の降順で取得
    List<ProductReview> findByRatingGreaterThanEqualAndIsApprovedTrueOrderByReviewDateDesc(Integer rating);

    // 2. 関連エンティティのプロパティを条件とする検索
    // 特定の顧客 ID によるレビューを検索
    List<ProductReview> findByCustomer_Id(Long customerId);

    // 3. 件数の取得（集計）
    // 特定の製品名に対するレビューの総数を取得
    Long countByProductName(String productName);

    // 4. 存在確認
    // 指定したお客が、特定の製品名に対してすでにレビュー済みか確認
    boolean existsByCustomer_IdAndProductName(Long customerId, String productName);

    // 5. 検索結果の件数制限
    // 最も高評価なレビューの上位5件を取得
    List<ProductReview> findByOrderByRatingDescIdAsc();
}
