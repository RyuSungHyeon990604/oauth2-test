package com.example.oauth2.global.user.repository;

import com.example.oauth2.global.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByOauthId(String oauthId);
}
