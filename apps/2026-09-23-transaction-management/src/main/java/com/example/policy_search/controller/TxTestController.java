package com.example.policy_search.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.policy_search.service.AccountService;
import com.example.policy_search.service.OuterService;
import com.example.policy_search.service.SelfCallService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tx")
@RequiredArgsConstructor
public class TxTestController {

    private final SelfCallService selfCallService;
    private final AccountService accountService;
    private final OuterService outerService;

    @PostMapping("/rename/{id}")
    public String rename(@PathVariable Long id, @RequestParam String name) {
        accountService.rename(id, name);
        return "ok";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromId, @RequestParam Long toId, @RequestParam Long amount) {
        accountService.transfer(fromId, toId, amount);
        return "ok";
    }

    @PostMapping("/propagation")
    public String propagation() {
        outerService.run();
        return "ok";
    }

    @PostMapping("/rollback")
    public String rollback(@RequestParam String type) throws Exception {
        switch (type) {
            case "checked" -> accountService.test1();
            case "unchecked" -> accountService.test2();
            case "rollbackFor" -> accountService.test3();
        }
        return "ok";
    }

    @PostMapping("/self/outer")
    public String selfOuter() {
        selfCallService.outer();
        return "ok";
    }

    @PostMapping("/self/inner")
    public String selfInner() {
        selfCallService.inner();  // 直接呼ぶ
        return "ok";
    }

    @PostMapping("/conflict/{id}")
    public String conflict(@PathVariable Long id) throws InterruptedException {
        accountService.conflictTest(id);
        return "ok";
    }

    @PostMapping("/lock/{id}")
    public String lock(@PathVariable Long id) throws InterruptedException {
        accountService.lockTest(id);
        return "ok";
    }

    @PostMapping("/deadlock")
    public String deadlock(@RequestParam Long firstId, @RequestParam Long secondId) throws InterruptedException {
        accountService.deadlockTest(firstId, secondId);
        return "ok";
    }

    @PostMapping("/dirtyread/{id}")
    public String dirtyread(@PathVariable Long id) throws InterruptedException {
        accountService.dirtyReadTest(id);
        return "ok";
    }
}
