package com.example.myapp.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Không có token → cho qua
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cắt token
        String token = authHeader.substring(7);

        // 4. Lấy username từ token
        String username = jwtService.extractUsername(token);

        // 5. Nếu chưa authenticate
        if (username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails user =
                userDetailsService.loadUserByUsername(username);

            // 6. Kiểm tra token hợp lệ
            if (jwtService.isTokenValid(token, user)) {

                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                    );

                // 7. Gắn user vào context
                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
            }
        }

        // 8. Cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}
