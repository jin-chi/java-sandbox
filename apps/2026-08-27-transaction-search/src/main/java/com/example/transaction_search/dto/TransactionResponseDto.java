package com.example.transaction_search.dto;

import java.time.LocalDateTime;

import com.example.transaction_search.entity.AccountTier;
import com.example.transaction_search.entity.Transaction;
import com.example.transaction_search.entity.TransactionStatus;
import com.example.transaction_search.entity.TransactionType;

public record TransactionResponseDto(
        Long id,
        String transactionRef,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        Long amount,
        LocalDateTime executedAt,
        String reviewedBy,
        Long accountId,
        String ownerName,
        AccountTier accountTier
) {
        public static TransactionResponseDto from(Transaction transaction) {
            return new TransactionResponseDto(
                transaction.getId(),
                transaction.getTransactionRef(),
                transaction.getType(),
                transaction.getStauts(),
                transaction.getAmount(),
                transaction.getExecutedAt(),
                transaction.getReviewedBy(),
                transaction.getAccount().getId(),
                transaction.getAccount().getOwnerName(),
                transaction.getAccount().getTier()
            );
        }
}
