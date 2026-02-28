package com.example.myapp.controllers;

import com.example.myapp.entities.User;
import com.example.myapp.services.TransactionExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionExportController {

    private final TransactionExportService exportService;

    @GetMapping("/export/pdf")
    public void exportPdf(
            @AuthenticationPrincipal User user,
            HttpServletResponse response
    ) throws Exception {

        response.setContentType("application/pdf");
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=transactions.pdf"
        );

        exportService.exportPdf(user, response.getOutputStream());
    }
}
