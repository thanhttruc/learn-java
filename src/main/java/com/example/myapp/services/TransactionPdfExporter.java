package com.example.myapp.services;

import com.example.myapp.entities.UserTransaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class TransactionPdfExporter {

    private Document document;
    private PdfPTable table;

    private static final NumberFormat VND_FORMAT =
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void start(OutputStream os) throws Exception {
        document = new Document();
        PdfWriter.getInstance(document, os);
        document.open();

        table = new PdfPTable(7);
        table.setWidthPercentage(100);

        addHeader();
        table.setWidths(new float[]{1.2f, 2f, 2.2f, 1.5f, 4f, 2.5f, 2f});
    }

    private void addHeader() {
        table.addCell("ID");
        table.addCell("Type");
        table.addCell("Amount");
        table.addCell("Account");
        table.addCell("Description");
        table.addCell("Time");
        table.addCell("Trạng thái");
    }

    public void addTransaction(UserTransaction tx) {

        table.addCell(tx.getId().toString());
        table.addCell(tx.getType().name());
        table.addCell(formatVnd(tx.getAmount()));

        table.addCell(
                tx.getAccount() != null
                        ? tx.getAccount().getId().toString()
                        : ""
        );

        table.addCell(
                tx.getDescription() == null ? "" : tx.getDescription()
        );

        table.addCell(
                tx.getTransactionTime() != null
                        ? tx.getTransactionTime().format(TIME_FORMAT)
                        : ""
        );

        table.addCell(
                tx.getStatus() != null ? tx.getStatus().name() : ""
        );
    }

//    private void addBodyCell(String text) {
//        PdfPCell cell = new PdfPCell(new Phrase(text, bodyFont));
//        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//        table.addCell(cell);
//    }

    private String formatVnd(BigDecimal amount) {
        if (amount == null) return "";
        return VND_FORMAT.format(amount);
    }

    public void finish() throws Exception {
        document.add(table);
        document.close();
    }
}