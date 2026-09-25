package com.example.policy_search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InnerService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        throw new RuntimeException("内側の失敗");
    }
}
