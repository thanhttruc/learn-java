package com.example.myapp.services;

import com.example.myapp.dtos.AccountRequest;
import com.example.myapp.dtos.AccountResponse;
import com.example.myapp.entities.Account;
import com.example.myapp.entities.User;
//import com.example.myapp.events.AccountCreatedEvent;
//import com.example.myapp.events.AccountEventPublisher;
import com.example.myapp.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
//    private final AccountEventPublisher eventPublisher;

    public AccountResponse createAccount(User user, AccountRequest req) {

        accountRepository.findByAccountNumber(req.getAccountNumber()).ifPresent(a -> {
            throw new RuntimeException("Category already exists");
        });

        Account account = Account.builder()
                .user(user)
                .name(req.getName())
                .type(req.getType())
                .bankName(req.getBankName())
                .branchName(req.getBranchName())
                .accountNumber(req.getAccountNumber())
                .currentBalance(req.getCurrentBalance())
                .currency(req.getCurrency())
                .build();

        Account saved = accountRepository.save(account);

//        eventPublisher.publish(new AccountCreatedEvent(
//                saved.getId(),
//                user.getId(),
//                saved.getType(),
//                saved.getInitialBalance(),
//                saved.getCurrency()
//        ));

        return toResponse(saved);
    }

    public List<AccountResponse> getAccounts(User user) {
        return accountRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public AccountResponse getAccountDetail(User user, Long accountId) {
        Account account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return toResponse(account);
    }

    public AccountResponse updateAccount(User user, Long accountId, AccountRequest req) {
        Account account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        accountRepository.findByAccountNumber(req.getAccountNumber()).ifPresent(a -> {
            throw new RuntimeException("Category already exists");
        });

        account.setName(req.getName());
        account.setBankName(req.getBankName());
        account.setBranchName(req.getBranchName());
        account.setAccountNumber(req.getAccountNumber());
        account.setCurrentBalance(req.getCurrentBalance());
        account.setCurrency(req.getCurrency());

        Account updated = accountRepository.save(account);
        return toResponse(updated);
    }

    public void deleteAccount(User user, Long accountId) {
        Account account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        accountRepository.delete(account);
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .type(account.getType())
                .bankName(account.getBankName())
                .branchName(account.getBranchName())
                .accountNumber(account.getAccountNumber())
                .currentBalance(account.getCurrentBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .build();
    }
}
