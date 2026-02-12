package com.example.myapp.dtos;

import com.example.myapp.entities.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        String name,

        @NotNull(message = "Category type is required")
        Category.CategoryType type

) {}

