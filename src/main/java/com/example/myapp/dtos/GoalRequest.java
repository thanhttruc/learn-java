package com.example.myapp.dtos;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalRequest {

    private String name;

    private BigDecimal targetAmount;

    private Long accountId;

    private LocalDate startDate;
    private LocalDate endDate;

}
