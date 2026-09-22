package com.example.policy_search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.policy_search.dto.PageResponse;
import com.example.policy_search.dto.PolicyRequestDto;
import com.example.policy_search.dto.PolicyResponseDto;
import com.example.policy_search.entity.Policy;
import com.example.policy_search.exception.PolicyNotFoundException;
import com.example.policy_search.repository.PolicyRepository;
import com.example.policy_search.repository.PolicySpecifications;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public PageResponse<PolicyResponseDto> policySearch(PolicyRequestDto req, Pageable pageable) {
        if (req.isEmpty())
            return PageResponse.from(Page.empty(pageable));

        Specification<Policy> spec = PolicySpecifications.containsPolicyNumber(req.getPolicyNumber())
                .and(PolicySpecifications.inProductTypes(req.getProductTypes()))
                .and(PolicySpecifications.premiumGreaterThanOrEqual(req.getPremiumFrom()))
                .and(PolicySpecifications.premiumLessThanOrEqual(req.getPremiumTo()))
                .and(PolicySpecifications.coverageGreaterThanOrEqual(req.getCoverageFrom()))
                .and(PolicySpecifications.coverageLessThanOrEqual(req.getCoverageTo()))
                .and(PolicySpecifications.containsFullName(req.getFullName()))
                .and(PolicySpecifications.hasSubscriberSegment(req.getSegment()))
                .and(PolicySpecifications.isUnCancelled(req.getActiveOnly()))
                .and(PolicySpecifications.containsKeyword(req.getKeyword()))
                .and(PolicySpecifications.hasApprovedStatus(req.getHasApprovedClaim()));

        Page<Policy> page = policyRepository.findAll(spec, pageable);

        if (page.isEmpty())
            throw new PolicyNotFoundException("検索条件に一致する契約が見つかりませんでした");

        return PageResponse.from(page.map(PolicyResponseDto::from));
    }
}
