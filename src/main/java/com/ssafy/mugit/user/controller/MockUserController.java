package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.service.MockUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/mocks")
@Tag(name = "User Test", description = "유저 테스트를 위한 가짜 회원가입 및 로그인 API입니다.")
@Slf4j
public class MockUserController {
    private final MockUserService mockUserService;

    @Operation(summary = "회원가입(Mock)", description = "(Mock)회원가입을 요청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mock 회원가입 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 회원가입 요청",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "409", description = "중복 닉네임",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @PostMapping("/regist")
    public ResponseEntity<MessageDto> regist(@RequestBody MockUserInfoDto mockUserInfoDto) {
        log.info("regist mock user: {}", mockUserInfoDto);
        mockUserService.createUserByUserInfo(mockUserInfoDto);
        return ResponseEntity.status(201).body(new MessageDto("Mock 회원가입 완료"));
    }

    @Operation(summary = "로그인(Mock)", description = "(Mock)로그인을 요청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mock 로그인 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 사용자 없음",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
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
