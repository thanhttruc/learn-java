package com.example.myapp.controllers;

import com.example.myapp.dtos.BudgetRequest;
import com.example.myapp.dtos.BudgetResponse;
import com.example.myapp.entities.Category;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.CategoryRepository;
import com.example.myapp.services.BudgetService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<BudgetResponse> create(
            @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User user
    ) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        BudgetResponse response = budgetService.createBudget(
                user,
                category,
                request.getAmountLimit(),
                request.getStartDate(),
                request.getEndDate()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAll(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(budgetService.getAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(budgetService.getById(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> update(
            @PathVariable Long id,
            @RequestBody BudgetRequest request,
            @AuthenticationPrincipal User user
    ) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        BudgetResponse response = budgetService.update(
                id,
                user,
                request.getAmountLimit(),
                request.getStartDate(),
                request.getEndDate(),
                category
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {

        budgetService.delete(id, user);

        return ResponseEntity.noContent().build();
    }
}
