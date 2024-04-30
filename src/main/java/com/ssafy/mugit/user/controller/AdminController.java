package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.web.dto.ListDto;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.UserSessionDto;
import com.ssafy.mugit.user.service.UserTotalSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자와 관련한 API입니다.")
public class AdminController {

    private final UserTotalSessionService userTotalSessionService;

    @Operation(summary = "세션 조회", description = "전체 세션을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "세션 조회 완료",
                    content = @Content(schema = @Schema(implementation = UserSessionDto.class))),
            @ApiResponse(responseCode = "401", description = "로그인 필요",
                    content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 부족",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/sessions")
    public ResponseEntity<ListDto<List<UserSessionDto>>> getSession() {
        List<UserSessionDto> allSession = userTotalSessionService.findAllSession();

        return ResponseEntity.status(200)
                .body(new ListDto<>(allSession));
    }
}

