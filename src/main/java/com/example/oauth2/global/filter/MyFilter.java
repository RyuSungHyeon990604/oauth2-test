package com.example.oauth2.global.filter;

import com.example.oauth2.global.auth.AuthUser;
import com.example.oauth2.global.auth.JwtAuthenticationToken;
import com.example.oauth2.global.util.CookieUtil;
import com.example.oauth2.global.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MyFilter extends OncePerRequestFilter {
    private final CookieUtil cookieUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = cookieUtil.getCookieValue(request, "access");
        if (accessToken != null) {
            handleAccessToken(accessToken);
        }
        filterChain.doFilter(request, response);
    }

    private void handleAccessToken(String token) {
        Long sub = Long.valueOf(JwtProvider.validateToken(token));
        AuthUser authUser = new AuthUser(sub);
        JwtAuthenticationToken tokenAuth = new JwtAuthenticationToken(authUser);
        SecurityContextHolder.getContext().setAuthentication(tokenAuth);
    }
}
