package com.example.transfer_api.dto;

import java.time.LocalDateTime;

import com.example.transfer_api.entity.Transfer;
import com.example.transfer_api.entity.TransferStatus;

public record TransferResponseDto(
    Long id,
    Long fromAccountId,
    String fromAccountNumber,
    Long toAccountId,
    String toAccountNumber,
    Long amount,
    TransferStatus status,
    LocalDateTime executedAt,
    String failureReason
) {
    public static TransferResponseDto from(Transfer transfer) {
        return new TransferResponseDto(
            transfer.getId(),
            transfer.getFromAccount().getId(),
            transfer.getFromAccount().getAccountNumber(),
            transfer.getToAccount().getId(),
            transfer.getToAccount().getAccountNumber(),
            transfer.getAmount(),
            transfer.getStatus(),
            transfer.getExecutedAt(),
            transfer.getFailureReason()
        );
    }
}
