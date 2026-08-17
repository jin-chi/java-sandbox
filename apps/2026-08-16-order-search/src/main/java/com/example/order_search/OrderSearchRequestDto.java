package com.example.order_search;

import java.time.LocalDate;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchRequestDto {

    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    private String orderNumber;

    @Size(max = 4, message = "4つまで指定可能です")
    private List<String> statuses;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Integer amountFrom;

    @Min(value = 0, message = "0以上の値を指定してください")
    private Integer amountTo;

    private LocalDate orderedFrom;
    private LocalDate orderedTo;

    @Size(min = 1, max = 100, message = "1文字以上100文字以下で指定してください")
    private String customerName;

    private String customerRank;

    public boolean isEmpty() {
        return !StringUtils.hasText(orderNumber)
                && CollectionUtils.isEmpty(statuses)
                && amountFrom == null
                && amountTo == null
                && orderedFrom == null
                && orderedTo == null
                && !StringUtils.hasText(customerName)
                && !StringUtils.hasText(customerRank);
    }
}
