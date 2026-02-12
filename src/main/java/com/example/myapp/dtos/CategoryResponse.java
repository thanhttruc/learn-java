package com.example.myapp.dtos;

import com.example.myapp.entities.Category.CategoryType;

import java.time.LocalDateTime;

public record CategoryResponse(
    Long id,
    String name,
    CategoryType type,
    LocalDateTime createdAt
) {}
