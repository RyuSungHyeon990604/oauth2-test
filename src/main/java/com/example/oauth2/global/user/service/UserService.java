package com.example.oauth2.global.user.service;

import com.example.oauth2.global.user.entity.Users;
import com.example.oauth2.global.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Users saveFromOAuth(String oauthId, String name){
        Users user = new Users(oauthId, name);
        return userRepository.save(user);
    }

    public Users getUserFromOAuth(String oauthId){
        return userRepository.findByOauthId(oauthId).orElse(null);
    }
}
