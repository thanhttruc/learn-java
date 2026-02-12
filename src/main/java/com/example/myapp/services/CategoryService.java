package com.example.myapp.services;

import com.example.myapp.dtos.CategoryRequest;
import com.example.myapp.dtos.CategoryResponse;
import com.example.myapp.entities.Category;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryResponse create(CategoryRequest request, User user) {

        if (categoryRepository.existsByUserIdAndNameIgnoreCase(
                user.getId(), request.name())) {
            throw new RuntimeException("Category already exists");
        }

        Category category = Category.builder()
                .name(request.name().trim())
                .type(request.type())
                .user(user)
                .build();

        Category saved = categoryRepository.save(category);
        System.out.println("ABC");
        return toResponse(saved);
    }


    public List<CategoryResponse> getAll(User user) {
        System.out.println("CategoryService.getAll - User ID: " + user.getId());
        return categoryRepository.findByUserId(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public CategoryResponse getById(Long id, User user) {
        System.out.println("CategoryService.getById - Category ID: " + id + ", User ID: " + user.getId());
        Category category = categoryRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return toResponse(category);
    }


    public CategoryResponse update(Long id, CategoryRequest request, User user) {

        Category category = categoryRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.name().trim());
        category.setType(request.type());

        Category saved = categoryRepository.save(category);

        return toResponse(saved);
    }


    public void delete(Long id, User user) {

        Category category = categoryRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(category);
    }


    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.getCreatedAt()
        );
    }
}
