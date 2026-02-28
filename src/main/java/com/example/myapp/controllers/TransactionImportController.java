package com.example.myapp.controllers;

import com.example.myapp.dtos.ImportResult;
import com.example.myapp.entities.User;
import com.example.myapp.services.TransactionImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionImportController {

    private final TransactionImportService importService;

    @PostMapping("/import")
    public ResponseEntity<ImportResult> importExcel(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(importService.importFile(file, user));
    }
}

