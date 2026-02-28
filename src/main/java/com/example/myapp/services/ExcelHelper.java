package com.example.myapp.services;

import com.example.myapp.dtos.TransactionImportRow;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelHelper {

    public List<TransactionImportRow> parse(MultipartFile file) {
        List<TransactionImportRow> rows = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Excel file is empty");
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() < 1) {
                throw new IllegalArgumentException("Excel does not contain data rows");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row r = sheet.getRow(i);
                if (r == null || isRowEmpty(r)) continue;

                TransactionImportRow row = TransactionImportRow.builder()
                        .accountId(getLong(r, 0))
                        .targetAccountId(getLongNullable(r, 1))
                        .categoryId(getLongNullable(r, 2))
                        .type(getString(r, 3))
                        .amount(getAmount(r, 4))
                        .description(getStringNullable(r, 5))
                        .transactionTime(getDateTime(r, 6))
                        .build();

                rows.add(row);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }

        return rows;
    }

    // ================= helpers =================

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i <= 6; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private String getString(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null) throw new RuntimeException("Column " + (index + 1) + " is required");
        return cell.getStringCellValue().trim();
    }

    private String getStringNullable(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        return cell.getStringCellValue().trim();
    }

    private Long getLong(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null) throw new RuntimeException("Column " + (index + 1) + " is required");
        return (long) cell.getNumericCellValue();
    }

    private Long getLongNullable(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null || cell.getCellType() == CellType.BLANK) return null;
        return (long) cell.getNumericCellValue();
    }

    private BigDecimal getAmount(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null) throw new RuntimeException("Amount is required");
        return BigDecimal.valueOf(cell.getNumericCellValue());
    }

    private LocalDateTime getDateTime(Row r, int index) {
        Cell cell = r.getCell(index);
        if (cell == null) return LocalDateTime.now();
        return cell.getLocalDateTimeCellValue();
    }
}
