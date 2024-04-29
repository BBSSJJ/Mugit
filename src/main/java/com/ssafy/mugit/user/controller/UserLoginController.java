package com.ssafy.mugit.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.requireNonNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저와 관련한 API입니다.")
public class UserLoginController {

    private final UserLoginService userLoginService;

    @Operation(summary = "로그인", description = "SNS 로그인을 요청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "302", description = "회원가입 필요",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "토큰인증 실패",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/login")
    public ResponseEntity<MessageDto> login(
            @RequestParam(defaultValue = "GOOGLE") SnsType snsType,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            HttpSession session) throws JsonProcessingException {

        // 사용자 로그인 : 사용자 session 등록 및 cookie 반환
        HttpHeaders cookieHeaders = userLoginService.login(token, snsType, session);

        // 회원가입 필요 시 Regist Cookie + 302 반환
        if (requireNonNull(cookieHeaders.get(HttpHeaders.SET_COOKIE)).get(0).contains("needRegist=true")) {
            return ResponseEntity.status(302)
                    .headers(cookieHeaders)
                    .body(new MessageDto("회원가입 필요"));
        }

        // 정상 로그인 시 Login Cookie + 200반환
        return ResponseEntity.status(200)
                .headers(cookieHeaders)
                .body(new MessageDto("로그인 완료"));
    }
}
