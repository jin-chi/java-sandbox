package com.example.policy_search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.policy_search.entity.Account;
import com.example.policy_search.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SelfCallService {

    private final AccountRepository repo;

    public void outer() {
        this.inner();
    }

    @Transactional
    public void inner() {
        Account a = repo.findById(1L).orElseThrow();
        a.setBalance(123L);
        throw new RuntimeException("失敗");
    }
}
