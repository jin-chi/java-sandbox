package com.example.transaction_search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_search.dto.PageResponse;
import com.example.transaction_search.dto.TransactionRequestDto;
import com.example.transaction_search.dto.TransactionResponseDto;
import com.example.transaction_search.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<TransactionResponseDto>> getTransactions(
            @ModelAttribute @Valid TransactionRequestDto req, Pageable pageable) {
        return ResponseEntity.ok(transactionService.transactionSearch(req, pageable));
    }
}
