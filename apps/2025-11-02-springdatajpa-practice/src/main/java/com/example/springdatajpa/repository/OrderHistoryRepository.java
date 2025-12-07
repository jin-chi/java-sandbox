package com.example.springdatajpa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.OrderHistory;

import jakarta.transaction.Transactional;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    // 1. リレーション検索 - 特定の Customer ID に紐づく注文を全て取得
    // エンティティ名（Customer）のフィールド（id）をアンダースコアで繋ぐ
    List<OrderHistory> findByCustomer_Id(Long customerId);

    // 2. 大小比較 - 指定金額以上の注文を取得
    List<OrderHistory> findByOrderAmountGreaterThanEqual(BigDecimal amount);

    // 3. 複数条件 AND - 特定の顧客IDの、特定のステータスの注文を取得
    List<OrderHistory> findByCustomer_IdAndStatus(Long customerId, String status);

    // 4. ソート - 特定ステータスの注文を注文日の降順（新しい順）で取得
    List<OrderHistory> findByStatusOrderByOrderDateDesc(String status);

    // 5. 削除 - 特定のステータスの注文を一括削除（削除操作には @Transaction が必要）
    @Transactional
    int deleteByStatus(String status);
}
