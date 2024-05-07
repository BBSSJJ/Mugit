package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.dto.GoogleUserInfoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ssafy.mugit.user.fixture.GoogleUserInfoFixture.DEFAULT_GOOGLE_USER_INFO;
import static com.ssafy.mugit.user.fixture.GoogleUserInfoFixture.NOT_REGISTERED_USER_INFO;

@RestController
public class MockGoogleController {

    @GetMapping("/oauth2/v2/userinfo")
    public ResponseEntity<GoogleUserInfoDto> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        GoogleUserInfoDto userInfo = DEFAULT_GOOGLE_USER_INFO.getFixture();
        GoogleUserInfoDto notRegisteredUserInfo = NOT_REGISTERED_USER_INFO.getFixture();
        if (accessToken.equals("qwerasdf1234") || accessToken.equals("valid_token")) return ResponseEntity.ok(userInfo);
        else if (accessToken.equals("not registered user token")) return ResponseEntity.ok(notRegisteredUserInfo);
        else return ResponseEntity.status(401).body(null);
    }
}
