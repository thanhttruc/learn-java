package com.example.myapp.controllers;

import com.example.myapp.dtos.GoalRequest;
import com.example.myapp.dtos.GoalResponse;
import com.example.myapp.entities.User;
import com.example.myapp.services.GoalService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<GoalResponse> create(
            @RequestBody GoalRequest request,
            @AuthenticationPrincipal User user
    ) {

        GoalResponse response = goalService.createGoal(
                user.getId(),
                request
        );

        return ResponseEntity.ok(response);
    }

    // ================= GET ALL =================
    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAll(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                goalService.getAllGoals(user.getId())
        );
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                goalService.getGoalById(user.getId(), id)
        );
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> update(
            @PathVariable Long id,
            @RequestBody GoalRequest request,
            @AuthenticationPrincipal User user
    ) {

        GoalResponse response = goalService.updateGoal(
                user.getId(),
                id,
                request
        );

        return ResponseEntity.ok(response);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {

        goalService.deleteGoal(user.getId(), id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    public ResponseEntity<List<GoalResponse>> getCompleted(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                goalService.getCompletedGoals(user.getId())
        );
    }

    @GetMapping("/incompleted")
    public ResponseEntity<List<GoalResponse>> getIncompleted(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(
                goalService.getIncompleteGoals(user.getId())
        );
    }

}
