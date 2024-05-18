package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.dto.GoogleUserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ssafy.mugit.fixure.GoogleUserInfoFixture.*;

@RestController
public class MockGoogleController {

    @GetMapping("/oauth2/v2/userinfo")
    public ResponseEntity<GoogleUserInfoDto> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        return switch (accessToken) {
            case "valid_token" -> ResponseEntity.ok(GOOGLE_USER_INFO.getFixture());
            case "valid_token_2" -> ResponseEntity.ok(GOOGLE_USER_INFO_2.getFixture());
            case "not registered user token" -> ResponseEntity.ok(NOT_REGISTERED_USER_INFO.getFixture());
            default -> ResponseEntity.status(401).body(null);
        };
    }
}
