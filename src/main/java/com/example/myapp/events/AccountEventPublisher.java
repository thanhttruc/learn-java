package com.example.myapp.events;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    public void publish(AccountCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                "account.exchange",
                "account.created",
                event
        );
    }
}