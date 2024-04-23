package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.service.UserRegistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRegistController {

    private final UserRegistService userRegistService;

    @PostMapping("profiles")
    public ResponseEntity<MessageDto> regist(
            @CookieValue(value = "snsId") String snsId,
            @CookieValue(value = "snsType") SnsType snsType,
            @RequestBody RegistProfileDto registProfileDto) {
        HttpHeaders cookieHeader = userRegistService.registAndGetLoginCookieHeader(snsId, snsType, registProfileDto);

        return ResponseEntity.status(201)
                .headers(cookieHeader)
                .body(new MessageDto("회원가입 완료"));
    }
}
