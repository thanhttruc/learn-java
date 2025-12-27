package com.example.myapp.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.myapp.entities.EmailQueue;
import com.example.myapp.repositories.EmailQueueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailQueueService {

    private final EmailQueueRepository repo;

    public void enqueue(
            String to,
            String subject,
            String body) {

        EmailQueue email = new EmailQueue();
        email.setRecipient(to);
        email.setSubject(subject);
        email.setBody(body);
        email.setStatus("PENDING");
        email.setRetryCount(0);
        email.setCreatedAt(LocalDateTime.now());
        email.setUpdatedAt(LocalDateTime.now());

        repo.save(email);
    }
}

