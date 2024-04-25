package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.fixture.UserInfoFixture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockGoogleController {

    @GetMapping("/oauth2/v2/userinfo")
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestParam(value = "access_token") String accessToken) {
        UserInfoDto userInfo = UserInfoFixture.DEFAULT_GOOGLE_USER_INFO.getUserInfo();
        UserInfoDto notRegisteredUserInfo = UserInfoFixture.NOT_REGISTERED_USER_INFO.getUserInfo();
        if (accessToken.equals("qwerasdf1234")) return ResponseEntity.ok(userInfo);
        else if (accessToken.equals("not registered user token")) return ResponseEntity.ok(notRegisteredUserInfo);
        else return ResponseEntity.status(401).body(null);
    }
}
