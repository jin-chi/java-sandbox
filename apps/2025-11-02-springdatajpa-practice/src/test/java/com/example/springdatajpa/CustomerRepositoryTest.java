package com.example.springdatajpa;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springdatajpa.entity.Customer;
import com.example.springdatajpa.repository.CustomerRepository;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testFindByEmailIgnoreCase() {
        // データ: 'taro.yamada@example.com' (ID: 1)
        // 大文字小文字を無視して検索できるか確認
        Optional<Customer> found = customerRepository.findByEmailIgnoreCase("Taro.YAMADA@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    void testFindByNameContaining() {
        // データ: 田中一郎(3), 田中健太(5)
        // 部分一致検索の確認
        List<Customer> customers = customerRepository.findByNameContaining("田中");
        assertThat(customers).hasSize(2);
    }

    @Test
    void testFindByAgeBetween() {
        // データ: 25歳から40歳: 山田太郎(35), 小林美咲(28)
        // 範囲検索の確認
        List<Customer> customers = customerRepository.findByAgeBetween(25, 40);
        assertThat(customers).hasSize(2);
        // assertThat(customers).extracting(Customer::getName).containsExactlyInAnyOrder("山田太郎", "田中一郎", "田中健太");
    }

    @Test
    void testFindByRegistrationDateGreaterThan() {
        // データ: 2023-01-01以降に登録された顧客の確認
        LocalDate cutoffDate = LocalDate.of(2023, 1, 1);
        List<Customer> customers = customerRepository.findByRegistrationDateGreaterThan(cutoffDate);

        assertThat(customers).hasSize(3);
        assertThat(customers).extracting(Customer::getName).containsExactlyInAnyOrder("佐藤花子", "小林美咲", "田中健太");
    }

    @Test
    void testFindByTop3AgeOrderByAgeDesc() {
        // データ: 48, 48, 35, 28, 24 のうち、上位3件を取得できるか確認
        List<Customer> top3 = customerRepository.findTop3ByOrderByAgeDesc();
        assertThat(top3).hasSize(3);

        // 年齢の降順になっているかを確認 (48, 48, 35)
        assertThat(top3.get(0).getAge()).isEqualTo(48);
        assertThat(top3.get(2).getAge()).isEqualTo(35);
    }

    @Test
    void testExistsByEmail() {
        // データが存在するか確認
        boolean exists = customerRepository.existsByEmail("hanako.sato@sample.jp");
        assertThat(exists).isTrue();

        // boolean notExists = customerRepository.existsByEmail("nonexistent@test.com");
        // assertThat(notExists).isFalse();
    }

    @Test
    void testFindByIsActiveAndAgeGreaterThan() {
        // データ: アクティブ(T)かつ年齢 > 30: 山田太郎(35)
        // 複数条件(AND)の確認
        List<Customer> customers = customerRepository.findByIsActiveAndAgeGreaterThan(true, 30);

        assertThat(customers).hasSize(2);
        assertThat(customers.get(0).getName()).isEqualTo("山田太郎", "田中健太");
    }
}