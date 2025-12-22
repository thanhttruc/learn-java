
package com.example.myapp.services;

import com.example.myapp.repositories.UserRepository;
import com.example.myapp.security.JwtService;
import com.example.myapp.dtos.LoginRequest;
import com.example.myapp.dtos.LoginResponse;
import com.example.myapp.dtos.RegisterRequest;
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

    private final AuthenticationManager authenticationManager;


    public void register( RegisterRequest req) {
        if (userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole(Role.USER);

        userRepo.save(user);

    }

    public LoginResponse login(LoginRequest req) {
        Authentication authentication =
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    req.username(),
                    req.password()
                )
            );

            UserDetails user =
            (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);
   }
}
