package com.example.myapp.worker;


import com.example.myapp.dtos.BudgetMessage;
import com.example.myapp.repositories.BudgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetWorker {

    private final BudgetRepository budgetRepository;

    @RabbitListener(queues = "budget.progress.queue")
    @Transactional
    public void handleExpense(BudgetMessage message) {


        int updatedRows = budgetRepository.increaseActualSpent(
                message.getUserId(),
                message.getCategoryId(),
                message.getTransactionTime(),
                message.getAmount()
        );

        if (updatedRows == 0) {
            log.info(
                    "No matching budget found for userId={}, categoryId={}, date={}",
                    message.getUserId(),
                    message.getCategoryId(),
                    message.getTransactionTime()
            );
        }
    }

}

