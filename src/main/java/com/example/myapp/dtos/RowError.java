package com.example.myapp.dtos;

import lombok.*;

@Data
@AllArgsConstructor
public class RowError {

    private int row;
    private String message;
}

