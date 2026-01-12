package com.example.myapp.entities;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "debt_loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DebtLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtType type;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private DebtStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum DebtType {
        DEBT, LOAN
    }
    
    public enum DebtStatus {
        ONGOING, PAID
    }
    
}

