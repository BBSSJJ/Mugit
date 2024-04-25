package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.service.MockUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/mocks")
@Slf4j
public class MockUserController {
    private final MockUserService mockUserService;

    @PostMapping("/regist")
    public ResponseEntity<MessageDto> regist(@RequestBody MockUserInfoDto mockUserInfoDto) {
        log.info("regist mock user: {}", mockUserInfoDto);
        mockUserService.createUserByUserInfo(mockUserInfoDto);
        return ResponseEntity.status(201).body(new MessageDto("Mock 회원가입 완료"));
    }

    @GetMapping("/login")
    public ResponseEntity<MessageDto> login(@RequestParam(name = "pk") Long userPk, HttpSession session) {
        log.info("login mock user: {}", userPk);

        // 사용자 로그인 : 사용자 session 등록 및 cookie 반환
        HttpHeaders cookieHeaders = mockUserService.login(userPk, session);

        return ResponseEntity.status(200)
                .headers(cookieHeaders)
                .body(new MessageDto("Mock 로그인 완료"));
    }
}
