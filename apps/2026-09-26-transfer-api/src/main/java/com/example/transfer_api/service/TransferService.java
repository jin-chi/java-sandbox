package com.example.transfer_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transfer_api.dto.TransferRequestDto;
import com.example.transfer_api.dto.TransferResponseDto;
import com.example.transfer_api.entity.Account;
import com.example.transfer_api.entity.Transfer;
import com.example.transfer_api.entity.TransferStatus;
import com.example.transfer_api.exception.AccountNotFoundException;
import com.example.transfer_api.exception.InsufficientBalanceException;
import com.example.transfer_api.exception.TransferNotFoundException;
import com.example.transfer_api.repository.AccountRepository;
import com.example.transfer_api.repository.TransferRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferResponseDto getTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow(() -> new TransferNotFoundException("指定された送金履歴が見つかりませんでした"));
        return TransferResponseDto.from(transfer);
    }

    @Transactional(noRollbackFor = InsufficientBalanceException.class)
    public TransferResponseDto transfer(TransferRequestDto req) {

        // ソート
        Long first = Math.min(req.getFromAccountId(), req.getToAccountId());
        Long second = Math.max(req.getFromAccountId(), req.getToAccountId());

        // 取得 (ロック)
        Account firstAccount = accountRepository.findByIdForUpdate(first).orElseThrow(() -> new AccountNotFoundException("指定された口座が見つかりませんでした"));
        Account secondAccount = accountRepository.findByIdForUpdate(second).orElseThrow(() -> new AccountNotFoundException("指定された口座が見つかりませんでした"));

        // from, to を判別
        Account from = req.getFromAccountId().equals(firstAccount.getId()) ? firstAccount : secondAccount;
        Account to = req.getFromAccountId().equals(firstAccount.getId()) ? secondAccount : firstAccount;

        // 残高不足チェック
        if (from.getBalance() < req.getAmount()) {
            transferRepository.save(Transfer.builder().fromAccount(from).toAccount(to).amount(req.getAmount()).status(TransferStatus.FAILED).executedAt(LocalDateTime.now()).failureReason("残高不足").build());
            throw new InsufficientBalanceException("残高が不足しています");
        }

        // set
        from.setBalance(from.getBalance() - req.getAmount());
        to.setBalance(to.getBalance() + req.getAmount());

        // transfers 更新
        Transfer saved = transferRepository.save(Transfer.builder().fromAccount(from).toAccount(to).amount(req.getAmount()).status(TransferStatus.COMPLETED).executedAt(LocalDateTime.now()).build());

        return TransferResponseDto.from(saved);
    }
}
