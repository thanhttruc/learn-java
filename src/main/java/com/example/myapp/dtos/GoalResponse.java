package com.example.myapp.dtos;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class GoalResponse {

    private Long id;
    private String name;
    private BigDecimal targetAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime createdAt;

    private Long userId;
    private Long accountId;
    private BigDecimal progress;
}
