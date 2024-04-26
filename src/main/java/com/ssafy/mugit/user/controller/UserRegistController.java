package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.request.RequestRegistProfileDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.service.UserRegistService;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저와 관련한 API입니다.")
public class UserRegistController {

    private final UserRegistService userRegistService;

    @Operation(summary = "회원가입", description = "회원가입을 요청한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 완료",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 회원가입 요청",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "409", description = "중복 닉네임",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @PostMapping("regist")
    public ResponseEntity<MessageDto> regist(
            @CookieValue(value = "needRegist") String needRegist,
            @CookieValue(value = "snsId") String snsId,
            @CookieValue(value = "snsType") SnsType snsType,
            @CookieValue(value = "email") String email,
            @RequestBody RequestRegistProfileDto requestRegistProfileDto,
            HttpServletRequest request) {
        
        HttpHeaders cookieHeader = userRegistService.registAndSetLogin(snsId, snsType, email, requestRegistProfileDto, request);

        return ResponseEntity.status(201)
                .headers(cookieHeader)
                .body(new MessageDto("회원가입 완료"));
    }
}
