package com.example.policy_search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscribers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Subscriber {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 加入者コード
    @Column(nullable = false)
    private String subscriberCode;

    // 氏名
    @Column(nullable = false)
    private String fullName;

    // 顧客区分
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriberSegment segment;
}
