package com.example.myapp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.EmailMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailQueueService {

    private final RabbitTemplate rabbitTemplate;

    public void enqueue(
            String to,
            String subject,
            String body) {

        EmailMessage emailMessage = new EmailMessage(to, subject, body);

        rabbitTemplate.convertAndSend("email.exchange", "email.send", emailMessage);
        System.out.println("Email message enqueued to RabbitMQ for: " + to);
    }
}

