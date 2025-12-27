package com.example.myapp.services;

import java.time.LocalDateTime;

import java.util.List;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.myapp.entities.EmailQueue;
import com.example.myapp.repositories.EmailQueueRepository;

import lombok.*;

@Service
@RequiredArgsConstructor
public class EmailWorker {

    private final EmailQueueRepository repo;
    private final JavaMailSender mailSender;

    @Scheduled(fixedDelay = 10000)
    public void processQueue() {

        List<EmailQueue> emails =
            repo.findTop10ByStatusOrderByCreatedAtAsc("PENDING");

        for (EmailQueue email : emails) {
            try {
                send(email);
                email.setStatus("SENT");
            } catch (Exception e) {
                email.setRetryCount(email.getRetryCount() + 1);
                email.setStatus("FAILED");
            }

            email.setUpdatedAt(LocalDateTime.now());
            repo.save(email);
        }
    }

    private void send(EmailQueue email) {

        SimpleMailMessage message =
            new SimpleMailMessage();

        message.setTo(email.getRecipient());
        message.setSubject(email.getSubject());
        message.setText(email.getBody());

        mailSender.send(message);
    }
}
