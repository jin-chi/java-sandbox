package com.example.policy_search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.policy_search.entity.Account;
import com.example.policy_search.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OuterService {

    private final InnerService innerService;
    private final AccountRepository repo;

    @Transactional
    public void run() {
        // 外側で更新
        Account a  = repo.findById(1L).orElseThrow();
        a.setBalance(999L);

        try {
            innerService.run();  // 内側で例外を投げる
        } catch (Exception e) {
            log.warn("catch した");
        }
    }
}
