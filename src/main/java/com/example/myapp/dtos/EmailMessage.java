package com.example.myapp.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailMessage {

    private String to;
    private String subject;
    private String content;
}

