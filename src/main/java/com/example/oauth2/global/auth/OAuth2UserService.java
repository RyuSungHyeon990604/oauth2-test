package com.example.oauth2.global.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("==== OAuth2UserService 실행 ====");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("Client Registration ID: {}", userRequest.getClientRegistration().getRegistrationId()); // ✅ 클라이언트 확인
        log.info("OAuth2 사용자 정보: {}", userRequest.getAdditionalParameters());


        // 카카오에서 가져온 사용자 정보 확인
        System.out.println("OAuth2 사용자 정보: " + oAuth2User.getAttributes());
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();
        String s = String.valueOf(attributes.get(userNameAttributeName));
        log.info("attributes.get() : {}",s);
        return new AuthUser(s);
    }
}
