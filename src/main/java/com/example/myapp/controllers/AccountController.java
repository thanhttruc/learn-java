package com.example.myapp.controllers;

import com.example.myapp.dtos.AccountRequest;
import com.example.myapp.dtos.AccountResponse;
import com.example.myapp.entities.User;
import com.example.myapp.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest request,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("User = " + user);
        return ResponseEntity.ok(accountService.createAccount(user, request));
    }


    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(accountService.getAccounts(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(accountService.getAccountDetail(user, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(accountService.updateAccount(user, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        accountService.deleteAccount(user, id);
        return ResponseEntity.noContent().build();
    }
}
