package com.example.oauth2.test;

import com.example.oauth2.global.auth.AuthUser;
import org.springframework.http.MediaType;
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
    public ResponseEntity<String> getLoginPage(@AuthenticationPrincipal AuthUser authUser) {
        // ✅ 로그인한 사용자가 있는 경우, 사용자 정보를 반환
        if (authUser != null) {
            return ResponseEntity.ok("로그인된 사용자: " + authUser.getName());
        }

        // ✅ 로그인 버튼 포함한 HTML 반환
        String loginPage = "<html>" +
                "<head><title>카카오 로그인</title></head>" +
                "<body>" +
                "<h2>카카오 로그인</h2>" +
                "<a href=\"/login/kakao\">" +
                "   <img src=\"https://developers.kakao.com/assets/img/about/logos/kakaologin/kr/kakao_account_login_btn_medium_narrow.png\" alt=\"카카오 로그인 버튼\" />" +
                "</a>" +
                "</body>" +
                "</html>";

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(loginPage);
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.ok("error");
    }
}
