package com.example.myapp.dtos;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionMessage implements Serializable {
    private Long transactionId;
}

