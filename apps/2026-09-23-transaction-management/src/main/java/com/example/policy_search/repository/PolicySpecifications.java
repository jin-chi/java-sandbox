package com.example.policy_search.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.policy_search.entity.Claim;
import com.example.policy_search.entity.ClaimStatus;
import com.example.policy_search.entity.Policy;
import com.example.policy_search.entity.ProductType;
import com.example.policy_search.entity.SubscriberSegment;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class PolicySpecifications {

    static public Specification<Policy> containsPolicyNumber(String policyNumber) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(policyNumber))
                return null;
            String keyword = "%" + policyNumber + "%";
            return cb.like(root.get("policyNumber"), keyword);
        };
    }

    static public Specification<Policy> inProductTypes(List<ProductType> productTypes) {
        return (root, query, cb) -> CollectionUtils.isEmpty(productTypes) ? null
                : root.get("productType").in(productTypes);
    }

    static public Specification<Policy> premiumGreaterThanOrEqual(Long premiumFrom) {
        return (root, query, cb) -> premiumFrom == null ? null
                : cb.greaterThanOrEqualTo(root.get("premium"), premiumFrom);
    }

    static public Specification<Policy> premiumLessThanOrEqual(Long premiumTo) {
        return (root, query, cb) -> premiumTo == null ? null
                : cb.lessThanOrEqualTo(root.get("premium"), premiumTo);
    }

    static public Specification<Policy> coverageGreaterThanOrEqual(LocalDate coverageFrom) {
        return (root, query, cb) -> coverageFrom == null ? null
                : cb.greaterThanOrEqualTo(root.get("endedOn"), coverageFrom);
    }

    static public Specification<Policy> coverageLessThanOrEqual(LocalDate coverageTo) {
        return (root, query, cb) -> coverageTo == null ? null
                : cb.lessThanOrEqualTo(root.get("startedOn"), coverageTo);
    }

    static public Specification<Policy> containsFullName(String fullName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(fullName))
                return null;
            String keyword = "%" + fullName + "%";
            return cb.like(root.get("subscriber").get("fullName"), keyword);
        };
    }

    static public Specification<Policy> hasSubscriberSegment(SubscriberSegment segment) {
        return (root, query, cb) -> segment == null ? null
                : cb.equal(root.get("subscriber").get("segment"), segment);
    }

    static public Specification<Policy> isUnCancelled(Boolean activeOnly) {
        return (root, query, cb) -> (activeOnly == null || !activeOnly) ? null
                : cb.isNull(root.get("cancelledAt"));
    }

    static public Specification<Policy> containsKeyword(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword))
                return null;
            String k = "%" + keyword + "%";
            return cb.or(
                    cb.like(root.get("policyNumber"), k),
                    cb.like(root.get("subscriber").get("fullName"), k));
        };
    }

    static public Specification<Policy> hasApprovedStatus(Boolean hasApprovedClaim) {
        return (root, query, cb) -> {
            if (hasApprovedClaim == null || !hasApprovedClaim)
                return null;
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Claim> subRoot = subquery.from(Claim.class);
            subquery.select(cb.literal(1));
            subquery.where(
                    cb.equal(subRoot.get("policy"), root),
                    cb.equal(subRoot.get("status"), ClaimStatus.APPROVED));
            return cb.exists(subquery);
        };
    }
}
