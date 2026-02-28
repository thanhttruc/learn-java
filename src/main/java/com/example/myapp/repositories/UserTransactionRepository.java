package com.example.myapp.repositories;

import com.example.myapp.entities.UserTransaction;
import com.example.myapp.entities.UserTransaction.TransactionStatus;
import com.example.myapp.entities.UserTransaction.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {

    List<UserTransaction> findByUserId(Long userId);

    List<UserTransaction> findByAccountId(Long accountId);

    Page<UserTransaction> findByUserIdOrderByTransactionTimeDesc(
            Long userId,
            Pageable pageable
    );

    List<UserTransaction> findByUserIdAndType(Long userId, TransactionType type);

    List<UserTransaction> findByUserIdAndStatus(Long userId, TransactionStatus status);

    List<UserTransaction> findByUserIdAndTransactionTimeBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );

    List<UserTransaction> findByUserIdAndTypeAndTransactionTimeBetween(
            Long userId,
            TransactionType type,
            LocalDate start,
            LocalDate end
    );

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM UserTransaction t
    WHERE t.user.id = :userId
      AND t.category.id = :categoryId
      AND t.type = 'EXPENSE'
      AND t.status = 'COMPLETED'
      AND t.createdAt BETWEEN :start AND :end
""")
    BigDecimal sumExpenseForBudget(
            Long userId,
            Long categoryId,
            LocalDateTime start,
            LocalDateTime end
    );
}

