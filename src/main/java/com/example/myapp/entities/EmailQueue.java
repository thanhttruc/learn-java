package com.example.myapp.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "email_queue")
@Getter @Setter
public class EmailQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipient;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String status;
    private int retryCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
