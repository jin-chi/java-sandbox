package com.example.transaction_search.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.transaction_search.entity.AccountTier;
import com.example.transaction_search.entity.Transaction;
import com.example.transaction_search.entity.TransactionType;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class TransactionSpecifications {

    public static Specification<Transaction> containsTransactionRef(String transactionRef) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(transactionRef))
                return cb.conjunction();
            String likeKeyword = "%" + transactionRef + "%";
            return cb.like(root.get("transactionRef"), likeKeyword);
        };
    }

    public static Specification<Transaction> inTypes(List<TransactionType> types) {
        return (root, query, cb) -> !CollectionUtils.isEmpty(types) ? root.get("type").in(types) : cb.conjunction();
    }

    public static Specification<Transaction> amountFrom(Long amountFrom) {
        return (root, query, cb) -> amountFrom != null ? cb.greaterThanOrEqualTo(root.get("amount"), amountFrom)
                : cb.conjunction();
    }

    public static Specification<Transaction> amountTo(Long amountTo) {
        return (root, query, cb) -> amountTo != null ? cb.lessThanOrEqualTo(root.get("amount"), amountTo)
                : cb.conjunction();
    }

    public static Specification<Transaction> executedFrom(LocalDate executedFrom) {
        return (root, query, cb) -> executedFrom != null
                ? cb.greaterThanOrEqualTo(root.get("executedAt"), executedFrom.atStartOfDay())
                : cb.conjunction();
    }

    public static Specification<Transaction> executedTo(LocalDate executedTo) {
        return (root, query, cb) -> executedTo != null
                ? cb.lessThan(root.get("executedAt"), executedTo.plusDays(1).atStartOfDay())
                : cb.conjunction();
    }

    public static Specification<Transaction> containsOwnerName(String ownerName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(ownerName))
                return cb.conjunction();
            String likeKeyword = "%" + ownerName + "%";
            return cb.like(root.get("account").get("ownerName"), likeKeyword);
        };
    }

    public static Specification<Transaction> hasAccountTier(AccountTier accountTier) {
        return (root, query, cb) -> accountTier != null ? cb.equal(root.get("account").get("tier"), accountTier)
                : cb.conjunction();
    }

    public static Specification<Transaction> isUnreviewed(Boolean unreviewed) {
        return (root, query, cb) -> {
            if (unreviewed == null || !unreviewed)
                return cb.conjunction();
            return cb.isNull(root.get("unreviewed"));
        };
    }

    public static Specification<Transaction> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return cb.conjunction();
            String likeKeyword = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("transactionRef"), likeKeyword),
                    cb.like(root.get("account").get("ownerName"), likeKeyword));
        };
    }

    public static Specification<Transaction> hasFailedTransaction(Boolean hasFailedTransaction) {
        return (root, query, cb) -> {
            if (hasFailedTransaction == null || !hasFailedTransaction)
                return cb.conjunction();
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Transaction> subRoot = subquery.from(Transaction.class);
            subquery.select(cb.literal(1));
            subquery.where(cb.equal(subRoot.get("status"), "FAILED"));
            return cb.exists(subquery);
        };
    }
}
