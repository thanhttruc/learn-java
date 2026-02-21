package com.example.myapp.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetRequest {

    private Long categoryId;
    private BigDecimal amountLimit;
    private LocalDate startDate;
    private LocalDate endDate;
}

