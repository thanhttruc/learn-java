package com.example.myapp.dtos;

import com.example.myapp.entities.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountResponse {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private AccountType type;
        private String bankName;
        private String branchName;
        private String accountNumber;
        private BigDecimal currentBalance;
        private String currency;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(AccountType type) {
            this.type = type;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder branchName(String branchName) {
            this.branchName = branchName;
            return this;
        }

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder currentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountResponse build() {
            AccountResponse response = new AccountResponse();
            response.id = this.id;
            response.name = this.name;
            response.type = this.type;
            response.bankName = this.bankName;
            response.branchName = this.branchName;
            response.accountNumber = this.accountNumber;
            response.currentBalance = this.currentBalance;
            response.currency = this.currency;
            response.createdAt = this.createdAt;
            return response;
        }
    }


    private Long id;
    private String name;
    private AccountType type;

    private String bankName;
    private String branchName;
    private String accountNumber;

    private BigDecimal currentBalance;
    private String currency;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return type;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
