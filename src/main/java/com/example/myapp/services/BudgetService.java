package com.example.myapp.services;

import com.example.myapp.dtos.BudgetResponse;
import com.example.myapp.entities.Budget;
import com.example.myapp.entities.Category;
import com.example.myapp.entities.User;
import com.example.myapp.producer.BudgetProducer;
import com.example.myapp.repositories.BudgetRepository;
import com.example.myapp.dtos.BudgetMessage;
import com.example.myapp.repositories.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetProducer budgetProducer;
    private final UserTransactionRepository transactionRepository;

    @Transactional
    public BudgetResponse createBudget(
            User user,
            Category category,
            BigDecimal amountLimit,
            LocalDate startDate,
            LocalDate endDate
    ) {

        validatePeriod(startDate, endDate);

        budgetRepository
                .findByUserAndCategoryAndPeriodStartAndPeriodEnd(
                        user, category, startDate, endDate
                )
                .ifPresent(b -> {
                    throw new RuntimeException("Budget already exists for this period");
                });


        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        BigDecimal actualSpent = transactionRepository
                .sumExpenseForBudget(
                        user.getId(),
                        category.getId(),
                        startDateTime,
                        endDateTime
                );

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .periodStart(startDate)
                .periodEnd(endDate)
                .limitAmount(amountLimit)
                .actualSpent(actualSpent)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(budgetRepository.save(budget));
    }

    public List<BudgetResponse> getAll(User user) {
        return budgetRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BudgetResponse getById(Long id, User user) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        validateOwner(budget, user);

        return toResponse(budget);
    }

    @Transactional
    public BudgetResponse update(
            Long id,
            User user,
            BigDecimal newLimit,
            LocalDate newStart,
            LocalDate newEnd,
            Category newCategory
    ) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        validateOwner(budget, user);
        validatePeriod(newStart, newEnd);

        LocalDateTime newStartDateTime = newStart.atStartOfDay();
        LocalDateTime newEndDateTime = newEnd.atTime(23, 59, 59);

        BigDecimal newActualSpent = transactionRepository
                .sumExpenseForBudget(
                        user.getId(),
                        newCategory.getId(),
                        newStartDateTime,
                        newEndDateTime
                );

        budget.setLimitAmount(newLimit);
        budget.setPeriodStart(newStart);
        budget.setPeriodEnd(newEnd);
        budget.setCategory(newCategory);
        budget.setLimitAmount(newActualSpent);

        return toResponse(budgetRepository.save(budget));
    }

    @Transactional
    public void delete(Long id, User user) {

        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        validateOwner(budget, user);

        budgetRepository.delete(budget);
    }


    private void validateOwner(Budget budget, User user) {
        if (!budget.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
    }

    private void validatePeriod(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("Start date must be before end date");
        }
    }

    private BudgetResponse toResponse(Budget budget) {

        double progress = 0;

        if (budget.getLimitAmount().compareTo(BigDecimal.ZERO) > 0) {
            progress = budget.getActualSpent()
                    .divide(budget.getLimitAmount(), 4, RoundingMode.HALF_UP)
                    .doubleValue() * 100;
        }

        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .amountLimit(budget.getLimitAmount())
                .actualSpent(budget.getActualSpent())
                .startDate(budget.getPeriodStart())
                .endDate(budget.getPeriodEnd())
                .progressPercent(progress)
                .build();
    }
}
