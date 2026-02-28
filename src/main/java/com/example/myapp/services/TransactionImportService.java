package com.example.myapp.services;

import com.example.myapp.dtos.ImportResult;
import com.example.myapp.dtos.RowError;
import com.example.myapp.dtos.TransactionImportRow;
import com.example.myapp.dtos.UserTransactionRequest;
import com.example.myapp.entities.User;
import com.example.myapp.entities.UserTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionImportService {

    private final ExcelHelper parser;
    private final UserTransactionService userTransactionService;

//    @Transactional
    public ImportResult importFile(MultipartFile file, User user) {

        List<TransactionImportRow> rows = parser.parse(file);

        int success = 0;
        List<RowError> errors = new ArrayList<>();

        int rowIndex = 2; // Excel data start row

        for (TransactionImportRow row : rows) {
            try {

                // ✅ validate + lấy type luôn
                UserTransaction.TransactionType type = validateRow(row);

                // ✅ map request
                UserTransactionRequest req = mapToRequest(row, type);

                // ✅ gọi service tạo transaction
                userTransactionService.create(user, req);

                success++;

            } catch (Exception e) {
                errors.add(new RowError(rowIndex, e.getMessage()));
            }

            rowIndex++;
        }

        return ImportResult.builder()
                .total(rows.size())
                .success(success)
                .errors(errors.isEmpty() ? null : errors)
                .build();
    }

    // ================= VALIDATE =================

    private UserTransaction.TransactionType validateRow(TransactionImportRow row) {

        if (row.getAccountId() == null) {
            throw new RuntimeException("accountId is required");
        }

        if (row.getAmount() == null ||
                row.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("amount must > 0");
        }

        if (row.getType() == null || row.getType().isBlank()) {
            throw new RuntimeException("type is required");
        }

        UserTransaction.TransactionType type;

        try {
            type = UserTransaction.TransactionType.valueOf(
                    row.getType().toUpperCase().trim()
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid transaction type: " + row.getType());
        }

        // business rule
        if (type == UserTransaction.TransactionType.TRANSFER) {
            if (row.getTargetAccountId() == null) {
                throw new RuntimeException("targetAccountId required for TRANSFER");
            }
        } else {
            if (row.getCategoryId() == null) {
                throw new RuntimeException("categoryId required for INCOME/EXPENSE");
            }
        }

        return type;
    }

    // ================= MAPPER =================

    private UserTransactionRequest mapToRequest(
            TransactionImportRow row,
            UserTransaction.TransactionType type) {

        return UserTransactionRequest.builder()
                .accountId(row.getAccountId())
                .targetAccountId(row.getTargetAccountId())
                .categoryId(row.getCategoryId())
                .type(type)
                .amount(row.getAmount())
                .description(row.getDescription())
                .transactionTime(
                        row.getTransactionTime() != null
                                ? row.getTransactionTime()
                                : LocalDateTime.now()
                )
                .build();
    }
}
