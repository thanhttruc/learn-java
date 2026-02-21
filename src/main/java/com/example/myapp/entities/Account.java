package com.example.myapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    private String bankName;
    private String branchName;
    private String accountNumber;


    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    private String currency;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 🔥 Optimistic Lock
    @Version
    @Column(nullable = false)
    private Long version;
}
