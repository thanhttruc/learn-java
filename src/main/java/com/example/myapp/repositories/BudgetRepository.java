package com.example.myapp.repositories;

import com.example.myapp.entities.Budget;
import com.example.myapp.entities.User;
import com.example.myapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {


    List<Budget> findByUser(User user);

    List<Budget> findByUserAndCategory(User user, Category category);

    List<Budget> findByUserAndPeriodStartGreaterThanEqualAndPeriodEndLessThanEqual(
            User user,
            LocalDateTime start,
            LocalDateTime end
    );

    Optional<Budget> findByUserAndCategoryAndPeriodStartAndPeriodEnd(
            User user,
            Category category,
            LocalDate periodStart,
            LocalDate periodEnd
    );

    @Modifying
    @Query("""
    UPDATE Budget b
    SET b.actualSpent = b.actualSpent + :amount
    WHERE b.user.id = :userId
      AND b.category.id = :categoryId
      AND :date BETWEEN b.periodStart AND b.periodEnd
""")
    int increaseActualSpent(
            Long userId,
            Long categoryId,
            LocalDate date,
            BigDecimal amount
    );

}

