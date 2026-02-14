package com.example.myapp.repositories;

import com.example.myapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myapp.entities. Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUser(User user);

    Optional<Account> findByIdAndUser(Long id, User user);

    Optional<Account> findByAccountNumber(String accountNumber);
}

