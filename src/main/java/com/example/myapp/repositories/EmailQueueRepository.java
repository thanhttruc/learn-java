package com.example.myapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myapp.entities.EmailQueue;

public interface EmailQueueRepository
        extends JpaRepository<EmailQueue, Long> {

    List<EmailQueue> findTop10ByStatusOrderByCreatedAtAsc(String status);
}

