package com.example.myapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myapp.entities.Goal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUserId(Long userId);

    List<Goal> findByAccountId(Long accountId);

    List<Goal> findByUserIdAndAccountId(Long userId, Long accountId);

    @Query("""
    SELECT g FROM Goal g
    WHERE g.user.id = :userId
      AND g.account.currentBalance < g.targetAmount
""")
    List<Goal> findIncompleteGoals(Long userId);



    @Query("""
    SELECT g FROM Goal g
    WHERE g.user.id = :userId
      AND g.account.currentBalance >= g.targetAmount
""")
    List<Goal> findCompletedGoals(Long userId);

}
