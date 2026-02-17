package com.example.myapp.controllers;

import com.example.myapp.dtos.UserTransactionRequest;
import com.example.myapp.dtos.UserTransactionResponse;
import com.example.myapp.entities.User;
import com.example.myapp.services.UserTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class UserTransactionController {

    private final UserTransactionService transactionService;

    // ================= CREATE =================

    @PostMapping
    public ResponseEntity<UserTransactionResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserTransactionRequest request
    ) {
        return ResponseEntity.ok(transactionService.create(user, request));
    }

    // ================= GET ALL =================

    @GetMapping
    public ResponseEntity<List<UserTransactionResponse>> getAll(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(transactionService.getAll(user));
    }

    // ================= GET BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<UserTransactionResponse> getById(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(transactionService.getById(user, id));
    }

    // ================= UPDATE =================

    @PutMapping("/{id}")
    public ResponseEntity<UserTransactionResponse> update(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UserTransactionRequest request
    ) {
        return ResponseEntity.ok(transactionService.update(user, id, request));
    }

    // ================= DELETE =================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        transactionService.delete(user, id);
        return ResponseEntity.noContent().build();
    }
}
