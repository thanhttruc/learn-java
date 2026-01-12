package com.example.myapp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(name = "initial_balance")
    private BigDecimal initialBalance;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    private String currency;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum AccountType {
        CASH, BANK, CREDIT_CARD
    }
}
