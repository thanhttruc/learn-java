package com.example.myapp.dtos;

import com.example.myapp.entities.AccountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class AccountRequest {

    private String name;
    private AccountType type;

    private String bankName;
    private String branchName;
    private String accountNumber;

    private BigDecimal currentBalance;
    private String currency;

}
