package com.example.myapp.dtos;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetMessage implements Serializable {

    // gửi thông tin transaction expense
    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate transactionTime;

}


