package com.example.transaction_search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_search.dto.PageResponse;
import com.example.transaction_search.dto.TransactionRequestDto;
import com.example.transaction_search.dto.TransactionResponseDto;
import com.example.transaction_search.entity.Transaction;
import com.example.transaction_search.exception.TransactionNotFoundException;
import com.example.transaction_search.repository.TransactionRepository;
import com.example.transaction_search.repository.TransactionSpecifications;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public PageResponse<TransactionResponseDto> transactionSearch(TransactionRequestDto req, Pageable pageable) {
        if (req.isEmpty())
            return PageResponse.from(Page.empty(pageable));

        Specification<Transaction> spec = TransactionSpecifications.containsTransactionRef(req.getTransactionRef())
                .and(TransactionSpecifications.inTypes(req.getTypes()))
                .and(TransactionSpecifications.amountGreaterThanOrEqual(req.getAmountFrom()))
                .and(TransactionSpecifications.amountLessThanOrEqual(req.getAmountTo()))
                .and(TransactionSpecifications.executedAtGreaterThanOrEqual(req.getExecutedFrom()))
                .and(TransactionSpecifications.executedAtLessThan(req.getExecutedTo()))
                .and(TransactionSpecifications.containsOwnerName(req.getOwnerName()))
                .and(TransactionSpecifications.hasAccountTier(req.getAccountTier()))
                .and(TransactionSpecifications.isUnreviewed(req.getUnreviewed()))
                .and(TransactionSpecifications.containsKeyword(req.getKeyword()))
                .and(TransactionSpecifications.hasFailedTransaction(req.getHasFailedTransaction()));

        Page<Transaction> page = transactionRepository.findAll(spec, pageable);

        if (page.isEmpty()) {
            throw new TransactionNotFoundException("検索条件に一致する取引が見つかりませんでした");
        }

        return PageResponse.from(page.map(TransactionResponseDto::from));
    }
}
