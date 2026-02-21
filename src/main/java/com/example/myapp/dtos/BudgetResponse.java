package com.example.myapp.dtos;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class BudgetResponse {

    private Long id;
    private Long categoryId;
    private String categoryName;

    private BigDecimal amountLimit;
    private BigDecimal actualSpent;

    private LocalDate startDate;
    private LocalDate endDate;

    private double progressPercent;
}