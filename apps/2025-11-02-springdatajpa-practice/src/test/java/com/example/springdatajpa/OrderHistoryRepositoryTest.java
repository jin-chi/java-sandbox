package com.example.springdatajpa;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springdatajpa.entity.OrderHistory;
import com.example.springdatajpa.repository.OrderHistoryRepository;

import jakarta.transaction.Transactional;

@DataJpaTest
public class OrderHistoryRepositoryTest {
    
    @Autowired
    OrderHistoryRepository orderHistoryRepository;

    // --- 1. リレーション検索（findByCustomer_Id） ---
    @Test
    void testFindByCustomer_Id() {
        // data.sql に基づき、Customer ID 1 (山田太郎) の注文履歴は3件あることを確認
        List<OrderHistory> orders = orderHistoryRepository.findByCustomer_Id(1L);
        // assertThat(orders).hasSize(3);
        assertThat(orders).as("顧客ID %d に紐づく注文IDがデータと一致しませんでした。", 1L)
                .extracting(OrderHistory::getId)
                .containsExactlyInAnyOrder(101L, 102L, 106L);

        // 取得した注文がID 1の顧客に紐づいているかを確認
        assertThat(orders.get(0).getCustomer().getId()).isEqualTo(1L);
    }

    // --- 2. 複数条件 AND（findByCustomer_IdAndStatus） ---
    @Test
    void testFindByCustomer_IdAndStatus() {
        // Customer ID 1 のうち、PENDING ステータスの注文を確認 (ID 106 の1件)
        List<OrderHistory> orders = orderHistoryRepository.findByCustomer_IdAndStatus(1L, "PENDING");
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getId()).isEqualTo(106L);
    }

    // --- 3. 大小比較（findByOrderAmountGreaterThanEqual） ---
    @Test
    void testFIndByOrderAmountGreaterThanEqual() {
        // 注文金額が10000.00円以上の注文を確認 (12000.50円, 15000.00円の2件)
        BigDecimal threshold = new BigDecimal("10000.00");
        List<OrderHistory> orders = orderHistoryRepository.findByOrderAmountGreaterThanEqual(threshold);
        assertThat(orders).hasSize(2);

        // 注文ID 102 (12000.50) と 105 (15000.00) が含まれることを確認
        assertThat(orders).extracting(OrderHistory::getId).containsExactly(102L, 105L);
    }

    // --- 4. ソート（findByStatusOrderByOrderDateDesc）---
    @Test
    void testFindByStatusOrderByOrderDateDesc() {
        // DELIVERED ステータスの注文を注文日の降順で取得できるか確認
        List<OrderHistory> deliveredOrders = orderHistoryRepository.findByStatusOrderByOrderDateDesc("DELIVERED");
        assertThat(deliveredOrders).hasSize(3);

        // 注文日の降順 (最新か最初) になっているかを確認
        // ID 105 (2024-06-25) > ID 103 (2024-06-15) > ID 101 (2024-06-01)
        assertThat(deliveredOrders.get(0).getId()).isEqualTo(105L);
        assertThat(deliveredOrders.get(2).getId()).isEqualTo(101L);
    }

    // --- 5. 削除クエリ（deleteByStatus） ---
    @Test
    @Transactional // 削除操作はトランザクション境界内で実行する必要がある
    void testDeleteByStatus() {
        long initialCount = orderHistoryRepository.count(); // 6件

        // PENDING ステータスの注文を削除 (ID 104, 106 の2件)
        int deleteCount = orderHistoryRepository.deleteByStatus("PENDING");
        assertThat(deleteCount).isEqualTo(2);

        // 削除後の件数を確認 (6 - 2 = 4件)
        long finalCound = orderHistoryRepository.count();
        assertThat(finalCound).isEqualTo(initialCount - 2);
    }
}
