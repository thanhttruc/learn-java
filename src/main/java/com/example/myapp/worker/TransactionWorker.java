package com.example.myapp.worker;


import com.example.myapp.configs.RabbitMQQueueConfig;
import com.example.myapp.dtos.TransactionMessage;
import com.example.myapp.dtos.UserTransactionRequest;
import com.example.myapp.entities.Account;
import com.example.myapp.entities.User;
import com.example.myapp.entities.UserTransaction;
import com.example.myapp.repositories.AccountRepository;
import com.example.myapp.repositories.UserRepository;
import com.example.myapp.repositories.UserTransactionRepository;
import com.example.myapp.services.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionWorker {

    private final UserTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @RabbitListener(queues = "transaction.created.queue")
    @Transactional
    public void consume(TransactionMessage message) {

        UserTransaction transaction = transactionRepository
                .findById(message.getTransactionId())
                .orElseThrow();

        try {

            Account account = transaction.getAccount();
            Account targetAccount = transaction.getTargetAccount();

            switch (transaction.getType()) {

                case INCOME -> {
                    account.setCurrentBalance(account.getCurrentBalance().add(transaction.getAmount()));
                }

                case EXPENSE -> {

                    if (account.getCurrentBalance().compareTo(transaction.getAmount()) < 0) {
                        throw new RuntimeException("Insufficient funds");
                    }

                    account.setCurrentBalance(account.getCurrentBalance().subtract(transaction.getAmount()));
                }

                case TRANSFER -> {

                    targetAccount = accountRepository.findById(
                            transaction.getTargetAccount().getId()
                    ).orElseThrow();

                    if (account.getCurrentBalance().compareTo(transaction.getAmount()) < 0) {
                        throw new RuntimeException("Insufficient funds");
                    }

                    account.setCurrentBalance(account.getCurrentBalance().subtract(transaction.getAmount()));
                    targetAccount.setCurrentBalance(targetAccount.getCurrentBalance().add(transaction.getAmount()));

                    accountRepository.save(targetAccount);
                }
            }

            accountRepository.save(account);

            transaction.setStatus(UserTransaction.TransactionStatus.COMPLETED);

        } catch (Exception e) {
            transaction.setStatus(UserTransaction.TransactionStatus.FAILED);
        }

        transactionRepository.save(transaction);
    }
}