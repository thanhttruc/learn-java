package com.example.myapp.services;

import com.example.myapp.entities.User;
import com.example.myapp.entities.UserTransaction;
import com.example.myapp.repositories.UserTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class TransactionExportService {

    private final UserTransactionRepository transactionRepository;
    private final TransactionPdfExporter pdfExporter;

    @Transactional(readOnly = true)
    public void exportPdf(User user, OutputStream os) throws Exception {

        final int PAGE_SIZE = 1000;

        pdfExporter.start(os);

        int page = 0;
        Page<UserTransaction> result;

        do {
            result = transactionRepository
                    .findByUserIdOrderByTransactionTimeDesc(
                            user.getId(),
                            PageRequest.of(page, PAGE_SIZE)
                    );

            for (UserTransaction tx : result.getContent()) {
                pdfExporter.addTransaction(tx);
            }

            page++;

        } while (!result.isLast());

        pdfExporter.finish();
    }
}