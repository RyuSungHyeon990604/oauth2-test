package com.example.oauth2.global.config;

import com.example.oauth2.global.auth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauth2.global.auth.OAuth2FailureHandler;
import com.example.oauth2.global.auth.OAuth2UserService;
import com.example.oauth2.global.auth.OAuthSuccessHandler;
import com.example.oauth2.global.filter.MyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuthSuccessHandler oauthSuccessHandler;
    private final OAuth2FailureHandler oauth2FailureHandler;
    private final MyFilter myFilter;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserService oauth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
            .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 로그인 비활성화
            .formLogin(AbstractHttpConfigurer::disable) // 기본 login form 비활성화
            .logout(AbstractHttpConfigurer::disable) // 기본 logout 비활성화
            .sessionManagement(sessionManagementConfigurer ->
                    sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용하지 않음
        //인가
        .authorizeHttpRequests(auth -> auth.requestMatchers("/error", "/").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class)
        //소셜로그인
        .oauth2Login(configurer -> configurer
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.baseUri("/login")//로그인페이지 주소설정
                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository))
                .redirectionEndpoint(redirectionEndpointConfig ->
                        redirectionEndpointConfig.baseUri("/login/oauth2/code/*"))///login/oauth2/code/* 를 가로채서 인증서버로부터 토큰을 받고 사용자 정보 조회? 요청
                .userInfoEndpoint(userInfoEndpointConfig ->
                        userInfoEndpointConfig.userService(oauth2UserService))//사용자 정보 받기
                .successHandler(oauthSuccessHandler)
                .failureHandler(oauth2FailureHandler));

        return http.build();
    }
}
