package com.example.productsbasic.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(

    @NotBlank
    @Size(max = 100)
    String name,

    @NotNull
    @DecimalMin(value = "0", inclusive = true)
    @Digits(integer = 10, fraction = 2, message = "10桁までかつ小数点2桁までの金額を指定してください")
    BigDecimal price,

    @NotNull
    @Min(value = 0, message = "0以上の数値を指定してください")
    Integer stock

) {}
