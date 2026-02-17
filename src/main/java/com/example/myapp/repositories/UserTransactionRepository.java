package com.example.myapp.repositories;

import com.example.myapp.entities.UserTransaction;
import com.example.myapp.entities.UserTransaction.TransactionStatus;
import com.example.myapp.entities.UserTransaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {

    List<UserTransaction> findByUserId(Long userId);

    List<UserTransaction> findByAccountId(Long accountId);

    List<UserTransaction> findByUserIdAndType(Long userId, TransactionType type);

    List<UserTransaction> findByUserIdAndStatus(Long userId, TransactionStatus status);

    List<UserTransaction> findByUserIdAndTransactionTimeBetween(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<UserTransaction> findByUserIdAndTypeAndTransactionTimeBetween(
            Long userId,
            TransactionType type,
            LocalDateTime start,
            LocalDateTime end
    );
}
