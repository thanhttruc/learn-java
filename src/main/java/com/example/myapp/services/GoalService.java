package com.example.myapp.services;

import com.example.myapp.dtos.GoalRequest;
import com.example.myapp.dtos.GoalResponse;
import com.example.myapp.entities.Account;
import com.example.myapp.entities.Goal;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.AccountRepository;
import com.example.myapp.repositories.GoalRepository;
import com.example.myapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    // ================= CREATE =================
    public GoalResponse createGoal(Long userId, GoalRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        validateDate(request);

        Goal goal = Goal.builder()
                .name(request.getName())
                .targetAmount(request.getTargetAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdAt(LocalDateTime.now())
                .user(user)
                .account(account)
                .build();

        return mapToResponse(goalRepository.save(goal));
    }

    // ================= GET BY ID =================
    @Transactional(readOnly = true)
    public GoalResponse getGoalById(Long userId, Long goalId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        validateOwner(userId, goal);

        return mapToResponse(goal);
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public List<GoalResponse> getAllGoals(Long userId) {
        return goalRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= UPDATE =================
    public GoalResponse updateGoal(Long userId, Long goalId, GoalRequest request) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        validateOwner(userId, goal);
        validateDate(request);

        if (request.getName() != null) {
            goal.setName(request.getName());
        }

        if (request.getTargetAmount() != null) {
            goal.setTargetAmount(request.getTargetAmount());
        }

        if (request.getStartDate() != null) {
            goal.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            goal.setEndDate(request.getEndDate());
        }

        if (request.getAccountId() != null) {
            Account account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            goal.setAccount(account);
        }

        return mapToResponse(goalRepository.save(goal));
    }

    // ================= DELETE =================
    public void deleteGoal(Long userId, Long goalId) {

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        validateOwner(userId, goal);

        goalRepository.delete(goal);
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> getCompletedGoals(Long userId) {
        return goalRepository.findCompletedGoals(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> getIncompleteGoals(Long userId) {
        return goalRepository.findIncompleteGoals(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= VALIDATE OWNER =================
    private void validateOwner(Long userId, Goal goal) {
        if (!goal.getUser().getId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
    }

    // ================= VALIDATE DATE =================
    private void validateDate(GoalRequest request) {
        if (request.getStartDate() != null &&
                request.getEndDate() != null &&
                request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date must be after start date");
        }
    }

    private GoalResponse mapToResponse(Goal goal) {

        BigDecimal progress = BigDecimal.ZERO;

        if (goal.getAccount().getCurrentBalance() != null
                && goal.getTargetAmount() != null
                && goal.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {

            progress = goal.getAccount().getCurrentBalance()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(goal.getTargetAmount(), 2, java.math.RoundingMode.HALF_UP);

            if (progress.compareTo(BigDecimal.valueOf(100)) > 0) {
                progress = BigDecimal.valueOf(100);
            }
        }

        return GoalResponse.builder()
                .id(goal.getId())
                .name(goal.getName())
                .targetAmount(goal.getTargetAmount())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .createdAt(goal.getCreatedAt())
                .userId(goal.getUser().getId())
                .accountId(goal.getAccount().getId())
                .progress(progress)
                .build();
    }

}
