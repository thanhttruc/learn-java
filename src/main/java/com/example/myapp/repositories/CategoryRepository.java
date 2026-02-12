package com.example.myapp.repositories;

import com.example.myapp.entities.Category;
import com.example.myapp.entities.Category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

    Optional<Category> findByIdAndUserId(Long id, Long userId);

    List<Category> findByUserIdAndType(Long userId, CategoryType type);

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

}
