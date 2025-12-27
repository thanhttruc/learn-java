package com.example.myapp.security;

import java.time.LocalDateTime;
import java.util.UUID;


import org.springframework.stereotype.Service;

import com.example.myapp.entities.RefreshToken;
import com.example.myapp.entities.User;
import com.example.myapp.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository repo;

    public RefreshToken create(User user) {

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setRevoked(false);
        token.setExpiredAt(
            LocalDateTime.now().plusDays(7)
        );

        return repo.save(token);
    }
    public RefreshToken verify(String token) {

        RefreshToken refreshToken = repo.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked())
            throw new RuntimeException("Token revoked");

        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token expired");

        return refreshToken;
    }

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repo.save(token);
    }
}
