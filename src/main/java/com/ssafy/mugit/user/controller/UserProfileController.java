package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저와 관련한 API입니다.")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Operation(summary = "프로필 정보 조회(자신)", description = "자신의 프로필 정보를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 + 프로필 정보",
                    content = @Content(schema = @Schema(implementation = ResponseUserProfileDto.class))),
            @ApiResponse(responseCode = "401", description = "Security Filter 통과 실패 : 로그인 정보 없음",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/profiles/detail")
    public ResponseEntity<ResponseUserProfileDto> getMyProfile(HttpSession session) {
        Long myId = (Long) session.getAttribute(SessionKeys.LOGIN_USER_SESSION_ID.getKey());
        return ResponseEntity.ok()
                .body(userProfileService.getProfileById(myId));
    }

    @Operation(summary = "프로필 정보 조회(타인)", description = "타인의 프로필 정보를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 + 프로필 정보",
                    content = @Content(schema = @Schema(implementation = ResponseUserProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 사용자 없음",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/{userId}/profiles/detail")
    public ResponseEntity<ResponseUserProfileDto> getUserProfile(@PathVariable Long userId) {
        return ResponseEntity.ok()
                .body(userProfileService.getProfileById(userId));
    }
}
