package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.fixture.GoogleUserInfoFixture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockGoogleController {

    @GetMapping("/oauth2/v2/userinfo")
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        UserInfoDto userInfo = GoogleUserInfoFixture.DEFAULT_USER_INFO.getUserInfo();
        if (accessToken.equals("qwerasdf1234")) return ResponseEntity.ok(userInfo);
        else return ResponseEntity.status(401).body(null);
    }
}
