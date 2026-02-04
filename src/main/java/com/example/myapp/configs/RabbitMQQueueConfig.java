package com.example.myapp.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQQueueConfig {

    // ===== Exchange =====
    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange("account.exchange");
    }

    @Bean
    public DirectExchange transactionExchange() {
        return new DirectExchange("transaction.exchange");
    }

    // ===== Queues =====
    @Bean
    public Queue accountCreatedQueue() {
        return QueueBuilder.durable("account.created.queue").build();
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return QueueBuilder.durable("transaction.created.queue").build();
    }

    // ===== Bindings =====
    @Bean
    public Binding accountCreatedBinding(
            Queue accountCreatedQueue,
            DirectExchange accountExchange
    ) {
        return BindingBuilder
                .bind(accountCreatedQueue)
                .to(accountExchange)
                .with("account.created");
    }

    @Bean
    public Binding transactionCreatedBinding(
            Queue transactionCreatedQueue,
            DirectExchange transactionExchange
    ) {
        return BindingBuilder
                .bind(transactionCreatedQueue)
                .to(transactionExchange)
                .with("transaction.created");
    }
}
