package com.example.myapp.controllers;

import com.example.myapp.dtos.AccountRequest;
import com.example.myapp.dtos.AccountResponse;
import com.example.myapp.entities.User;
import com.example.myapp.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse createAccount(
            @RequestBody AccountRequest request,
            @AuthenticationPrincipal User user
    ) {
        System.out.println("User = " + user);
        return accountService.createAccount(user, request);
    }


    // Lấy danh sách tài khoản
    @GetMapping
    public List<AccountResponse> getAccounts(
            @AuthenticationPrincipal User user
    ) {
        return accountService.getAccounts(user);
    }

    // Lấy chi tiết tài khoản
    @GetMapping("/{id}")
    public AccountResponse getAccountDetail(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return accountService.getAccountDetail(user, id);
    }

    // Cập nhật tài khoản
    @PutMapping("/{id}")
    public AccountResponse updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest request,
            @AuthenticationPrincipal User user
    ) {
        return accountService.updateAccount(user, id, request);
    }

    // Xóa tài khoản
    @DeleteMapping("/{id}")
    public void deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        accountService.deleteAccount(user, id);
    }
}
