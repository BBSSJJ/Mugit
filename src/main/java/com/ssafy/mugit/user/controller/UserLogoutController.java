package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.service.UserLogoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "유저와 관련한 API입니다.")
public class UserLogoutController {

    private final UserLogoutService userLogoutService;

    @Operation(summary = "로그아웃", description = "로그아웃을 요청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "Security Filter 통과 실패 : 로그인 정보 없음",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/logout")
    public ResponseEntity<MessageDto> logout(HttpServletRequest request) {
        HttpHeaders cookieHeader = userLogoutService.logout(request);
        return ResponseEntity.status(200)
                .headers(cookieHeader)
                .body(new MessageDto("로그아웃 완료"));
    }
}
