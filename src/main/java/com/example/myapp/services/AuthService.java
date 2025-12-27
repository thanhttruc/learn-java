
package com.example.myapp.services;

import com.example.myapp.repositories.UserRepository;
import com.example.myapp.security.JwtService;
import com.example.myapp.security.RefreshTokenService;
import com.example.myapp.dtos.LoginRequest;
import com.example.myapp.dtos.LoginResponse;
import com.example.myapp.dtos.RegisterRequest;
import com.example.myapp.entities.RefreshToken;
import com.example.myapp.entities.Role;
import com.example.myapp.entities.User;

import lombok.RequiredArgsConstructor;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class  AuthService {
    
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;


    public void register( RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username exists");
        }

        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.USER);

        userRepo.save(user);

    }

    public LoginResponse login(LoginRequest req) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                req.username(),
                req.password()
            )
        );

        User user = userRepo.findByUsername(req.username()).orElseThrow();
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new LoginResponse(accessToken, refreshToken.getToken());
   }
}
