package com.example.policy_search.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.policy_search.entity.ProductType;
import com.example.policy_search.entity.SubscriberSegment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyRequestDto {

    @Size(min = 1, max = 100, message = "1~100文字で指定してください")
    private String policyNumber;

    @Size(min = 1, max = 4, message = "1~4要素で指定してください")
    private List<ProductType> productTypes;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Long premiumFrom;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Long premiumTo;

    private LocalDate coverageFrom;
    private LocalDate coverageTo;

    @Size(min = 1, max = 100, message = "1~100文字で指定してください")
    private String fullName;

    private SubscriberSegment segment;

    private Boolean activeOnly;

    @Size(min = 1, max = 100, message = "1~100文字で指定してください")
    private String keyword;

    private Boolean hasApprovedClaim;

    public boolean isEmpty() {
        return !StringUtils.hasText(policyNumber)
                && CollectionUtils.isEmpty(productTypes)
                && premiumFrom == null
                && premiumTo == null
                && coverageFrom == null
                && coverageTo == null
                && !StringUtils.hasText(fullName)
                && segment == null
                && (activeOnly == null || !activeOnly)
                && !StringUtils.hasText(keyword)
                && (hasApprovedClaim == null || !hasApprovedClaim);
    }
}
