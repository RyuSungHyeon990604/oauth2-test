package com.example.oauth2.global.auth;

import com.example.oauth2.global.util.CookieUtil;
import com.example.oauth2.global.util.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OauthSuccessHandler implements AuthenticationSuccessHandler {

    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        AuthUser principal =(AuthUser) authentication.getPrincipal();
        log.info("=== 로그인 성공 ===");
        log.info("obj to String: {}", principal.toString());

        String accessToken = JwtProvider.generateToken(principal.getName(), 99999);
        Cookie access = cookieUtil.create("access", accessToken, 99999);
        response.addCookie(access);

        response.sendRedirect("/");
    }
}
