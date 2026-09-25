package com.example.policy_search.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.policy_search.entity.Policy;
import com.example.policy_search.entity.PolicyStatus;
import com.example.policy_search.entity.ProductType;
import com.example.policy_search.entity.SubscriberSegment;

public record PolicyResponseDto(
    Long id,
    String policyNumber,
    ProductType productType,
    PolicyStatus status,
    Long premium,
    LocalDate startedOn,
    LocalDate endedOn,
    LocalDateTime cancelledAt,
    Long subscriberId,
    String subscriberCode,
    String fullName,
    SubscriberSegment segment
) {

    public static PolicyResponseDto from(Policy policy) {
        return new PolicyResponseDto(
            policy.getId(),
            policy.getPolicyNumber(),
            policy.getProductType(),
            policy.getStatus(),
            policy.getPremium(),
            policy.getStartedOn(),
            policy.getEndedOn(),
            policy.getCancelledAt(),
            policy.getSubscriber().getId(),
            policy.getSubscriber().getSubscriberCode(),
            policy.getSubscriber().getFullName(),
            policy.getSubscriber().getSegment()
        );
    }
}
