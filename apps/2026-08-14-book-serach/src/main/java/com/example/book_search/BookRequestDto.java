package com.example.book_search;

import org.hibernate.validator.constraints.Range;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record BookRequestDto(

        @Size(min = 1, max = 100, message = "1文字以上100文字以内で指定してください")
        String keyword,

        @Size(min = 1, max = 100, message = "1文字以上100文字以内で指定してください")
        String publisher,

        @Min(value = 0, message = "0以上の値を指定してください")
        Integer priceMax,

        @Range(min = 1000, max = 9999, message = "1000以上、9999未満の値を指定してください")
        Integer yearFrom,

        @Range(min = 1000, max = 9999, message = "1000以上、9999未満の値を指定してください")
        Integer yearTo

) {
    public boolean isEmpty() {
        return !StringUtils.hasText(keyword)
                && !StringUtils.hasText(publisher)
                && priceMax == null
                && yearFrom == null
                && yearTo == null;
    }
}