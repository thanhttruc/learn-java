package com.example.myapp.entities;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@RequiredArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    private User user;

    private boolean revoked;

    private LocalDateTime expiredAt;
}
