package com.example.myapp.dtos;

import com.example.myapp.entities.UserTransaction.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class UserTransactionRequest {

    @NotNull(message = "Account id is required")
    private Long accountId;

    // Chỉ dùng cho TRANSFER
    private Long targetAccountId;

    // Chỉ dùng cho INCOME / EXPENSE
    private Long categoryId;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String description;

    private LocalDateTime transactionTime;
}
