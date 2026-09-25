package com.example.policy_search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.policy_search.entity.Account;
import com.example.policy_search.repository.AccountRepository;

@Service
// @Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void rename(Long id, String name) {
        Account a = repo.findById(id).orElseThrow();
        a.setName(name);
    }

    // @Transactional
    public void transfer(Long fromId, Long toId, Long amount) {
        Account from = repo.findById(fromId).orElseThrow();
        Account to = repo.findById(toId).orElseThrow();

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
    }

    @Transactional
    public void test1() throws Exception {
        Account a = repo.findById(1L).orElseThrow();
        a.setBalance(777L);
        throw new Exception("検査例外");
    }

    @Transactional
    public void test2() {
        Account a = repo.findById(1L).orElseThrow();
        a.setBalance(888L);
        throw new RuntimeException("非検査例外");
    }

    @Transactional(rollbackFor = Exception.class)
    public void test3() throws Exception {
        Account a = repo.findById(1L).orElseThrow();
        a.setBalance(999L);
        throw new Exception("検査例外 + rollbackFor");
    }

    @Transactional
    public void conflictTest(Long id) throws InterruptedException {
        Account a = repo.findById(id).orElseThrow();
        Thread.sleep(10000);  // 10秒待つ
        a.setBalance(a.getBalance() - 10000);
    }

    @Transactional
    public void lockTest(Long id) throws InterruptedException {
        Account a = repo.findByIdForUpdate(id).orElseThrow();
        Thread.sleep(10000);
        a.setBalance(a.getBalance() - 100);
    }
}
