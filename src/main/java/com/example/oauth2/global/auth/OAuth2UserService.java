package com.example.oauth2.global.auth;

import com.example.oauth2.global.user.entity.Users;
import com.example.oauth2.global.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("==== OAuth2UserService 실행 ====");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("Client Registration ID: {}", userRequest.getClientRegistration().getRegistrationId()); // ✅ 클라이언트 확인
        log.info("OAuth2 사용자 정보: {}", userRequest.getAdditionalParameters());


        // 카카오에서 가져온 사용자 정보 확인
        log.info("OAuth2 사용자 정보: " + oAuth2User.getAttributes());
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String attributeName = userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String oauthId = String.valueOf(attributes.get(attributeName));
        String name = getProfileName(attributes, provider);

        //oauthId에 해당하는 사용자가없으면 db에 저장
        Users user = userRepository.findByOauthId(oauthId)
                .orElseGet(()->save(oauthId,name));

        return new AuthUser(user.getId());
    }

    private String getProfileName(Map<String, Object> attributes, String provider) {
        Map<String, Object> profile = (Map<String, Object>) attributes.get("properties");
        String res = String.valueOf(UUID.randomUUID());
        switch (provider) {
            case "google":
                res = (String) profile.get("name");
                break;
            case "kakao":
                res = (String) profile.get("nickname");
                break;
            default:
                break;
        }

        return res;
    }

    private Users save(String oauthId, String name) {
        log.info("can not find user by oauth id");
        log.info("=== 신규 소셜계정 추가 ===", oauthId);
        Users user = new Users(oauthId, name);
        return userRepository.save(user);
    }
}
