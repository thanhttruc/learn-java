package com.example.myapp.services;

import com.example.myapp.dtos.TransactionMessage;
import com.example.myapp.dtos.UserTransactionRequest;
import com.example.myapp.dtos.UserTransactionResponse;
import com.example.myapp.entities.*;
import com.example.myapp.producer.TransactionProducer;
import com.example.myapp.repositories.AccountRepository;
import com.example.myapp.repositories.CategoryRepository;
import com.example.myapp.repositories.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final UserTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionProducer transactionProducer;

    // ================= CREATE =================

    @Transactional
    public UserTransactionResponse create(User user, UserTransactionRequest request) {

        Account account = accountRepository
                .findByIdAndUser(request.getAccountId(), user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        UserTransaction transaction = new UserTransaction();
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionTime(
                request.getTransactionTime() != null
                        ? request.getTransactionTime()
                        : LocalDateTime.now()
        );
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(UserTransaction.TransactionStatus.PENDING);

        // 🔥 Handle TRANSFER
        if (request.getType() == UserTransaction.TransactionType.TRANSFER) {

            if (request.getTargetAccountId() == null) {
                throw new RuntimeException("Target account is required for transfer");
            }

            Account targetAccount = accountRepository
                    .findByIdAndUser(request.getTargetAccountId(), user)
                    .orElseThrow(() -> new RuntimeException("Target account not found"));

            if (targetAccount.getId().equals(account.getId())) {
                throw new RuntimeException("Source and target account cannot be the same");
            }

            transaction.setTargetAccount(targetAccount);
            transaction.setCategory(null);

        } else {
            transaction.setTargetAccount(null);
            transaction.setCategory(category);
        }

        UserTransaction saved = transactionRepository.save(transaction);

        sendToQueue(saved.getId());

        return new UserTransactionResponse(saved);
    }

    // ================= READ ALL =================

    public List<UserTransactionResponse> getAll(User user) {
        return transactionRepository.findByUserId(user.getId())
                .stream()
                .map(UserTransactionResponse::new)
                .toList();
    }

    // ================= READ BY ID =================

    public UserTransactionResponse getById(User user, Long id) {

        UserTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        validateOwnership(user, transaction);

        return new UserTransactionResponse(transaction);
    }

    // ================= UPDATE =================

    @Transactional
    public UserTransactionResponse update(User user, Long id, UserTransactionRequest request) {

        UserTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        validateOwnership(user, transaction);

        if (transaction.getStatus() == UserTransaction.TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction is processing");
        }

        // Nếu đã COMPLETED → rollback trước
        if (transaction.getStatus() == UserTransaction.TransactionStatus.COMPLETED) {
            rollbackTransaction(transaction);
        }

        Account account = accountRepository
                .findByIdAndUser(request.getAccountId(), user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        transaction.setAccount(account);
        transaction.setType(request.getType());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionTime(
                request.getTransactionTime() != null
                        ? request.getTransactionTime()
                        : LocalDateTime.now()
        );
        transaction.setStatus(UserTransaction.TransactionStatus.PENDING);

        if (request.getType() == UserTransaction.TransactionType.TRANSFER) {

            if (request.getTargetAccountId() == null) {
                throw new RuntimeException("Target account is required for transfer");
            }

            Account targetAccount = accountRepository
                    .findByIdAndUser(request.getTargetAccountId(), user)
                    .orElseThrow(() -> new RuntimeException("Target account not found"));

            if (targetAccount.getId().equals(account.getId())) {
                throw new RuntimeException("Source and target account cannot be the same");
            }

            transaction.setTargetAccount(targetAccount);
            transaction.setCategory(null);

        } else {
            transaction.setTargetAccount(null);
            transaction.setCategory(category);
        }

        UserTransaction saved = transactionRepository.save(transaction);

        sendToQueue(saved.getId());

        return new UserTransactionResponse(saved);
    }

    // ================= DELETE =================

    @Transactional
    public void delete(User user, Long id) {

        UserTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        validateOwnership(user, transaction);

        if (transaction.getStatus() == UserTransaction.TransactionStatus.COMPLETED) {
            rollbackTransaction(transaction);
        }

        transactionRepository.delete(transaction);
    }

    // ================= ROLLBACK =================

    private void rollbackTransaction(UserTransaction transaction) {

        Account account = transaction.getAccount();

        switch (transaction.getType()) {

            case INCOME -> {
                account.setCurrentBalance(
                        account.getCurrentBalance().subtract(transaction.getAmount())
                );
            }

            case EXPENSE -> {
                account.setCurrentBalance(
                        account.getCurrentBalance().add(transaction.getAmount())
                );
            }

            case TRANSFER -> {

                Account targetAccount = transaction.getTargetAccount();

                account.setCurrentBalance(
                        account.getCurrentBalance().add(transaction.getAmount())
                );

                targetAccount.setCurrentBalance(
                        targetAccount.getCurrentBalance().subtract(transaction.getAmount())
                );

                accountRepository.save(targetAccount);
            }
        }

        accountRepository.save(account);
    }

    // ================= UTIL =================

    private void validateOwnership(User user, UserTransaction transaction) {
        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
    }

    private void sendToQueue(Long transactionId) {
        TransactionMessage message = new TransactionMessage();
        message.setTransactionId(transactionId);
        transactionProducer.send(message);
    }
}
