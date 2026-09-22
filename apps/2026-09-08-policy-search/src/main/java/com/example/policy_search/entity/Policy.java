package com.example.policy_search.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "policies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Policy {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 証券番号
    @Column(nullable = false)
    private String policyNumber;

    // 外部キー (Subscriber テーブルの Primary Key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriber_id", nullable = false)
    private Subscriber subscriber;

    // 商品種別
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;

    // 契約ステータス
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    // 月額保険料
    @Column(nullable = false)
    private Long premium;

    // 契約開始日
    @Column(nullable = false)
    private LocalDate startedOn;

    // 契約終了日
    @Column(nullable = false)
    private LocalDate endedOn;

    // 解約日時
    private LocalDateTime cancelledAt;
}
