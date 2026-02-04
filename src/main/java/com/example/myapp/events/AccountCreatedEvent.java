package com.example.myapp.events;

import com.example.myapp.entities.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class AccountCreatedEvent {

    private Long accountId;
    private Long userId;
    private AccountType type;
    private BigDecimal initialBalance;
    private String currency;

    public AccountCreatedEvent(Long accountId, Long userId, AccountType type, BigDecimal initialBalance, String currency) {
        this.accountId = accountId;
        this.userId = userId;
        this.type = type;
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}

