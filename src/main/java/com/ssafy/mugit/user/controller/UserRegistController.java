package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.service.UserRegistService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRegistController {

    private final UserRegistService userRegistService;

    @PostMapping("regist")
    public ResponseEntity<MessageDto> regist(
            @CookieValue(value = "needRegist") String needRegist,
            @CookieValue(value = "snsId") String snsId,
            @CookieValue(value = "snsType") SnsType snsType,
            @CookieValue(value = "email") String email,
            @RequestBody RegistProfileDto registProfileDto,
            HttpSession httpSession) {

        if (!Boolean.parseBoolean(needRegist)) return ResponseEntity.status(400).build();

        HttpHeaders cookieHeader = userRegistService.registAndSetLogin(snsId, snsType, email, registProfileDto, httpSession);

        return ResponseEntity.status(201)
                .headers(cookieHeader)
                .body(new MessageDto("회원가입 완료"));
    }
}
