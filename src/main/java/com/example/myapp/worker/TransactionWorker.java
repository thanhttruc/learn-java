package com.example.myapp.worker;

import com.example.myapp.dtos.BudgetMessage;
import com.example.myapp.dtos.TransactionMessage;
import com.example.myapp.entities.Account;
import com.example.myapp.entities.UserTransaction;
import com.example.myapp.producer.BudgetProducer;
import com.example.myapp.repositories.AccountRepository;
import com.example.myapp.repositories.UserTransactionRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionWorker {

    private final UserTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BudgetProducer budgetProducer;

    @RabbitListener(queues = "transaction.created.queue")
    @Retryable(
            value = OptimisticLockException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 200)
    )
    @Transactional
    public void consume(TransactionMessage message) {

        UserTransaction transaction = transactionRepository
                .findById(message.getTransactionId())
                .orElseThrow();

        if (transaction.getStatus() == UserTransaction.TransactionStatus.COMPLETED) {
            return;
        }

        try {

            Account account = accountRepository
                    .findById(transaction.getAccount().getId())
                    .orElseThrow();

            switch (transaction.getType()) {

                case INCOME -> {
                    account.setCurrentBalance(
                            account.getCurrentBalance().add(transaction.getAmount())
                    );
                }

                case EXPENSE -> {

                    if (account.getCurrentBalance().compareTo(transaction.getAmount()) < 0) {
                        throw new RuntimeException("Insufficient funds");
                    }

                    account.setCurrentBalance(
                            account.getCurrentBalance().subtract(transaction.getAmount())
                    );
                }

                case TRANSFER -> {

                    Account targetAccount = accountRepository
                            .findById(transaction.getTargetAccount().getId())
                            .orElseThrow();

                    if (account.getCurrentBalance().compareTo(transaction.getAmount()) < 0) {
                        throw new RuntimeException("Insufficient funds");
                    }

                    account.setCurrentBalance(
                            account.getCurrentBalance().subtract(transaction.getAmount())
                    );

                    targetAccount.setCurrentBalance(
                            targetAccount.getCurrentBalance().add(transaction.getAmount())
                    );

                    accountRepository.save(targetAccount);
                }
            }

            accountRepository.save(account);

            transaction.setStatus(UserTransaction.TransactionStatus.COMPLETED);

            if (transaction.getType() == UserTransaction.TransactionType.EXPENSE) {

                BudgetMessage budgetMessage = new BudgetMessage();
                budgetMessage.setUserId(transaction.getUser().getId());
                budgetMessage.setCategoryId(transaction.getCategory().getId());
                budgetMessage.setAmount(transaction.getAmount());
                budgetMessage.setTransactionTime(transaction.getTransactionTime().toLocalDate());

                budgetProducer.send(budgetMessage);
            }



        } catch (OptimisticLockException e) {

            log.warn("Optimistic lock conflict, retrying... Transaction ID: {}",
                    transaction.getId());

            throw e;

        } catch (Exception e) {

            log.error("Transaction failed permanently: {}", transaction.getId(), e);

            transaction.setStatus(UserTransaction.TransactionStatus.FAILED);
        }

        transactionRepository.save(transaction);
    }
}
