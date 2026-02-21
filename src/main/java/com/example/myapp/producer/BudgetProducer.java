package com.example.myapp.producer;

import com.example.myapp.dtos.BudgetMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(BudgetMessage message) {
        rabbitTemplate.convertAndSend("budget.exchange", "budget.progress", message);
    }
}

