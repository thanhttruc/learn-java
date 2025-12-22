package com.example.myapp.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {

    private static final String SECRET_KEY = "my-secret-key-123456";

    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("role", userDetails.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(
                new Date(System.currentTimeMillis() + 1000 * 60 * 60)
            )
            .signWith(
                Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                SignatureAlgorithm.HS256
            )
            .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername());
    }
}
