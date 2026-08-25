package com.example.order_search.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.order_search.entity.CustomerRank;
import com.example.order_search.entity.Order;
import com.example.order_search.entity.OrderStatus;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class OrderSpecifications {

    public static Specification<Order> orderNumberContains(String orderNumber) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(orderNumber))
                return cb.conjunction();
            String n = "%" + orderNumber + "%";
            return cb.like(root.get("orderNumber"), n);
        };
    }

    public static Specification<Order> statusesIn(List<OrderStatus> statuses) {
        return (root, query, cb) -> !CollectionUtils.isEmpty(statuses) ? root.get("status").in(statuses)
                : cb.conjunction();
    }

    public static Specification<Order> amountFrom(Integer amountFrom) {
        return (root, query, cb) -> amountFrom != null ? cb.greaterThanOrEqualTo(root.get("totalAmount"), amountFrom)
                : cb.conjunction();
    }

    public static Specification<Order> amountTo(Integer amountTo) {
        return (root, query, cb) -> amountTo != null ? cb.lessThanOrEqualTo(root.get("totalAmount"), amountTo)
                : cb.conjunction();
    }

    public static Specification<Order> orderedFrom(LocalDate orderedFrom) {
        return (root, query, cb) -> orderedFrom != null
                ? cb.greaterThanOrEqualTo(root.get("orderedAt"), orderedFrom.atStartOfDay())
                : cb.conjunction();
    }

    public static Specification<Order> orderedTo(LocalDate orderedTo) {
        return (root, query, cb) -> orderedTo != null
                ? cb.lessThan(root.get("orderedAt"), orderedTo.plusDays(1).atStartOfDay())
                : cb.conjunction();
    }

    public static Specification<Order> customerNameContains(String customerName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(customerName))
                return cb.conjunction();
            String name = "%" + customerName + "%";
            return cb.like(root.get("customer").get("name"), name);
        };
    }

    public static Specification<Order> customerRankEquals(CustomerRank customerRank) {
        return (root, query, cb) -> customerRank != null
                ? cb.equal(root.get("customer").get("rank"), customerRank)
                : cb.conjunction();
    }

    public static Specification<Order> keywordContains(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return cb.conjunction();
            String k = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("orderNumber"), k),
                    cb.like(root.get("customer").get("name"), k));
        };
    }

    public static Specification<Order> hasPendingOrder(Boolean hasPendingOrder) {
        return (root, query, cb) -> {
            if (hasPendingOrder == null || !hasPendingOrder) return cb.conjunction();
            Subquery<Integer> sub = query.subquery(Integer.class);
            Root<Order> subRoot = sub.from(Order.class);
            sub.select(cb.literal(1));
            sub.where(
                cb.equal(subRoot.get("customer"), root.get("customer")),
                cb.equal(subRoot.get("status"), OrderStatus.PENDING)
            );
            return cb.exists(sub);
        };
    }
}