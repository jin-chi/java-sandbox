package com.example.transaction_search.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.transaction_search.entity.AccountTier;
import com.example.transaction_search.entity.TransactionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequestDto {

    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    private String transactionRef;

    @Size(min = 1, max = 4, message = "1つ以上4つ以下で指定してください")
    private List<TransactionType> types;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Long amountFrom;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Long amountTo;

    private LocalDate executedFrom;
    private LocalDate executedTo;

    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    private String ownerName;

    private AccountTier accountTier;
    private Boolean unreviewed;
    
    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    private String keyword;

    private Boolean hasFailedTransaction;

    public boolean isEmpty() {
        return !StringUtils.hasText(transactionRef)
                && CollectionUtils.isEmpty(types)
                && amountFrom == null
                && amountTo == null
                && executedFrom == null
                && executedTo == null
                && !StringUtils.hasText(ownerName)
                && accountTier == null
                && (unreviewed == null || !unreviewed)
                && !StringUtils.hasText(keyword)
                && (hasFailedTransaction == null || !hasFailedTransaction);
    }
}
