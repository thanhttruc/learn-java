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

    private BigDecimal initialBalance;
    private String currency;
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setType(AccountType type) {
//        this.type = type;
//    }
//
//    public void setBankName(String bankName) {
//        this.bankName = bankName;
//    }
//
//    public void setBranchName(String branchName) {
//        this.branchName = branchName;
//    }
//
//    public void setAccountNumber(String accountNumber) {
//        this.accountNumber = accountNumber;
//    }
//
//    public void setInitialBalance(BigDecimal initialBalance) {
//        this.initialBalance = initialBalance;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }
}
