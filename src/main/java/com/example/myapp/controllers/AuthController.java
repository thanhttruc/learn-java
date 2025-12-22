package com.example.myapp.controllers;

import com.example.myapp.dtos.LoginRequest;
import com.example.myapp.dtos.RegisterRequest;
import com.example.myapp.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody RegisterRequest req) {

        authService.register(req);
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody LoginRequest req) {
        authService.login(req);
        return ResponseEntity.ok("Login success");
    }

    
}
