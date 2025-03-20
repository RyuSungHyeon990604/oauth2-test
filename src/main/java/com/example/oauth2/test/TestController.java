package com.example.oauth2.test;

import com.example.oauth2.global.auth.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/users")
    public ResponseEntity<AuthUser> hello(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(authUser);
    }

    @GetMapping
    public ResponseEntity<AuthUser> getUser(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(authUser);
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.ok("error");
    }
}
