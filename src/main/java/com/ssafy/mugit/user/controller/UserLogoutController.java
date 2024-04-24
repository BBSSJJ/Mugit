package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.service.UserLogoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/")
public class UserLogoutController {
    private final UserLogoutService userLogoutService;

    @GetMapping("/logout")
    public ResponseEntity<MessageDto> logout(HttpServletRequest request) {
        HttpHeaders cookieHeader = userLogoutService.logout(request);
        return ResponseEntity.status(200)
                .headers(cookieHeader)
                .body(new MessageDto("로그아웃 완료"));
    }
}
