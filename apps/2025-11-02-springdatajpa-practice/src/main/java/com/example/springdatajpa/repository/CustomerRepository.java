package com.example.springdatajpa.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springdatajpa.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // 1. 文字列検索（大文字/小文字無視） - 厳密な結果を得るため Optional でラップ
    Optional<Customer> findByEmailIgnoreCase(String email);

    // 2. 部分一致検索 - 名前に指定の文字列を含む顧客を検索
    List<Customer> findByNameContaining(String keyword);

    // 3. 範囲検索 - 指定された年齢範囲内の顧客を検索
    List<Customer> findByAgeBetween(int minAge, int maxAge);

    // 4. 日付範囲検索 - 特定の日付より後に登録した顧客を検索
    List<Customer> findByRegistrationDateGreaterThan(LocalDate date);

    // 5. ソートと制限 - 年齢が高い順に上位3名を取得
    List<Customer> findTop3ByOrderByAgeDesc();

    // 6. 存在チェック - 指定のメールアドレスを持つ顧客が存在するか確認
    boolean existsByEmail(String email);

    // 7. 複数条件 - 有効かつ指定年齢以上の顧客を検索
    List<Customer> findByIsActiveAndAgeGreaterThan(boolean isActive, int age);

}
