package com.example.product_search;

import org.springframework.util.StringUtils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(

        @Size(min = 1, max = 100, message = "1文字以上100文字以内で指定してください")
        String name,
        @Size(min = 1, max = 100, message = "1文字以上100文字以内で指定してください")
        String category,
        @Min(value = 0, message = "0以上の値を指定してください")
        Integer priceFrom,
        @Min(value = 0, message = "0以上の値を指定してください")
        Integer priceTo,
        Boolean inStock
    ) {
        public boolean isEmpty() {
            return !StringUtils.hasText(name)
                    && !StringUtils.hasText(category)
                    && priceFrom == null
                    && priceTo == null
                    && inStock == null;
        }
}
