package com.example.myapp.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.EmailMessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailQueueService {

    private final RabbitTemplate rabbitTemplate;

    public void enqueue(
            String to,
            String subject,
            String body) {

        EmailMessageDto emailMessage = new EmailMessageDto(to, subject, body);

        rabbitTemplate.convertAndSend("email.exchange", "email.send", emailMessage);
        System.out.println("Email message enqueued to RabbitMQ for: " + to);
    }
}

