package com.example.transfer_api.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferResponseDto getTransfer(Long id) {
        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("指定された送金履歴が見つかりませんでした"));
        return TransferResponseDto.from(transfer);
    }

    @Transactional(noRollbackFor = InsufficientBalanceException.class)
    public TransferResponseDto transfer(TransferRequestDto req) {

        // デッドロック回避：ロックは必ず口座IDの昇順で取る。
        // from/to の順で取ると、逆向きの同時送金で循環待ちになる。
        Long first = Math.min(req.getFromAccountId(), req.getToAccountId());
        Long second = Math.max(req.getFromAccountId(), req.getToAccountId());

        // 取得 (ロック)
        Account firstAccount = accountRepository.findByIdForUpdate(first)
                .orElseThrow(() -> new AccountNotFoundException("指定された口座が見つかりませんでした"));
        Account secondAccount = accountRepository.findByIdForUpdate(second)
                .orElseThrow(() -> new AccountNotFoundException("指定された口座が見つかりませんでした"));

        // from, to を判別
        boolean fromIsFirst = req.getFromAccountId().equals(firstAccount.getId());
        Account from = fromIsFirst ? firstAccount : secondAccount;
        Account to = fromIsFirst ? secondAccount : firstAccount;

        // DB に合わせて datetime(6) に丸める
        LocalDateTime executedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);

        // 残高不足：FAILED を記録してから例外を投げる。
        // noRollbackFor によりコミットされるため、この判定は必ず setBalance より前に置くこと。
        if (from.getBalance() < req.getAmount()) {
            transferRepository.save(Transfer.builder()
                    .fromAccount(from)
                    .toAccount(to)
                    .amount(req.getAmount())
                    .status(TransferStatus.FAILED)
                    .executedAt(executedAt)
                    .failureReason("残高不足")
                    .build());
            throw new InsufficientBalanceException("残高が不足しています");
        }

        // set
        from.setBalance(from.getBalance() - req.getAmount());
        to.setBalance(to.getBalance() + req.getAmount());

        // transfers 更新
        Transfer saved = transferRepository.save(Transfer.builder()
                .fromAccount(from)
                .toAccount(to)
                .amount(req.getAmount())
                .status(TransferStatus.COMPLETED)
                .executedAt(executedAt)
                .build());

        return TransferResponseDto.from(saved);
    }
}
