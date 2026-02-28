package com.example.myapp.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImportResult {

    private int total;
    private int success;
    private List<RowError> errors;
}


