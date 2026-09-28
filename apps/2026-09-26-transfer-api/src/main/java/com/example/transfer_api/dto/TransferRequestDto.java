package com.example.transfer_api.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransferRequestDto {

    @NotNull(message = "必須の指定項目です")
    private Long fromAccountId;

    @NotNull(message = "必須の指定項目です")
    private Long toAccountId;

    @NotNull(message = "必須の指定項目です")
    @Min(value = 1, message = "1以上の値を指定してください")
    private Long amount;

    public boolean isEmpty() {
        return fromAccountId == null
                && toAccountId == null
                && amount == null;
    }

    @AssertTrue(message = "送金元と送金先に同じ口座を指定することできません")
    public boolean isDifferentAccount() {
        if (fromAccountId == null || toAccountId == null)
                return true;
        return !fromAccountId.equals(toAccountId);
    }
}
