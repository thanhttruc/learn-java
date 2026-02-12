package com.example.myapp.controllers;

import com.example.myapp.dtos.CategoryRequest;
import com.example.myapp.dtos.CategoryResponse;
import com.example.myapp.entities.Category;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.CategoryRepository;

import com.example.myapp.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @RequestBody CategoryRequest request,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("ABC");
        return ResponseEntity.ok(categoryService.create(request, user));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.getAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.getById(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @RequestBody CategoryRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(categoryService.update(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        categoryService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
