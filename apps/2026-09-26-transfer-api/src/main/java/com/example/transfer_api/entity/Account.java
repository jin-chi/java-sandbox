package com.example.transfer_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "accounts",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_accounts_account_number",
            columnNames = {"account_number"}
        )
    })
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private Long balance;

    @Version
    private Long version;
}
