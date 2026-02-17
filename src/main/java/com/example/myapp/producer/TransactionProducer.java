package com.example.myapp.producer;


import com.example.myapp.configs.RabbitMQQueueConfig;
import com.example.myapp.dtos.TransactionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(TransactionMessage message) {
        rabbitTemplate.convertAndSend("transaction.exchange", "transaction.created", message);
    }
}

