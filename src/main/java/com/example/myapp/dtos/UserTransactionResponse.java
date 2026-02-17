package com.example.myapp.dtos;

import com.example.myapp.entities.UserTransaction;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserTransactionResponse {

    private Long id;

    private Long accountId;
    private String accountName;

    private Long targetAccountId;
    private String targetAccountName;

    private Long categoryId;
    private String categoryName;

    private UserTransaction.TransactionType type;

    private BigDecimal amount;

    private String description;

    private UserTransaction.TransactionStatus status;

    private LocalDateTime transactionTime;

    private LocalDateTime createdAt;


    public UserTransactionResponse(UserTransaction transaction) {

        this.id = transaction.getId();

        this.accountId = transaction.getAccount().getId();
        this.accountName = transaction.getAccount().getName();

        this.targetAccountId = transaction.getTargetAccount() != null
                ? transaction.getTargetAccount().getId()
                : null;

        this.targetAccountName = transaction.getTargetAccount() != null
                ? transaction.getTargetAccount().getName()
                : null;

        this.categoryId = transaction.getCategory() != null
                ? transaction.getCategory().getId()
                : null;

        this.categoryName = transaction.getCategory() != null
                ? transaction.getCategory().getName()
                : null;

        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.status = transaction.getStatus();
        this.transactionTime = transaction.getTransactionTime();
        this.createdAt = transaction.getCreatedAt();
    }
}