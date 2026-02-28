package com.example.myapp.controllers;

import com.example.myapp.dtos.LoginRequest;
import com.example.myapp.dtos.LoginResponse;
import com.example.myapp.dtos.RefreshTokenRequest;
import com.example.myapp.dtos.RegisterRequest;
import com.example.myapp.entities.RefreshToken;
import com.example.myapp.entities.User;
import com.example.myapp.security.JwtService;
import com.example.myapp.security.RefreshTokenService;
import com.example.myapp.services.AuthService;
import com.example.myapp.producer.EmailQueueService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final EmailQueueService emailQueueService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        emailQueueService.enqueue(
        req.getEmail(),
        "Verify your account",
        "Welcome! Please verify your email."
    );
        return ResponseEntity.ok("Register success");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refresh(@RequestBody RefreshTokenRequest req) {
        RefreshToken token = refreshTokenService.verify(req.refreshToken());
        User user = token.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new LoginResponse(newAccessToken, token.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
        @RequestBody RefreshTokenRequest req) {

        RefreshToken token =
        refreshTokenService.verify(req.refreshToken());

        refreshTokenService.revoke(token);

        return ResponseEntity.ok("Logout success");
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file
    ) throws Exception {

        String url = authService.uploadAvatar(user, file);
        return ResponseEntity.ok(url);
    }

}
