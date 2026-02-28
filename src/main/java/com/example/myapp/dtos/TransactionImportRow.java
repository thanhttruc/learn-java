package com.example.myapp.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionImportRow {

    private Long accountId;
    private Long targetAccountId;
    private Long categoryId;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionTime;
}

