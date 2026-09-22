package com.example.policy_search.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.policy_search.dto.PageResponse;
import com.example.policy_search.dto.PolicyRequestDto;
import com.example.policy_search.dto.PolicyResponseDto;
import com.example.policy_search.service.PolicyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<PolicyResponseDto>> getPolicies(@ModelAttribute @Valid PolicyRequestDto req,
            Pageable pageable) {
        return ResponseEntity.ok(policyService.policySearch(req, pageable));
    }
}
