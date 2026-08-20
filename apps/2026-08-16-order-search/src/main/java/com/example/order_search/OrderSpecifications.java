package com.example.order_search;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        return (root, query, cb) -> orderedFrom != null ? cb.greaterThanOrEqualTo(root.get("orderedAt"), orderedFrom)
                : cb.conjunction();
    }

    public static Specification<Order> orderedTo(LocalDate orderedTo) {
        return (root, query, cb) -> orderedTo != null ? cb.lessThan(root.get("orderedAt"), orderedTo.plusDays(1))
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
}