package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.service.UserFindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@Tag(name = "User Test", description = "유저 테스트를 위한 가짜 회원가입 및 로그인 API입니다.")
public class UserFindController {

    private final UserFindService userFindService;

    @Operation(summary = "프로필 정보 조회(닉네임)", description = "해당 닉네임의 프로필 정보를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 + 프로필 정보",
                    content = @Content(schema = @Schema(implementation = ResponseUserProfileDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 사용자 없음",
                    content = @Content(schema = @Schema(implementation = MessageDto.class)))
    })
    @GetMapping("/nick/{nickName}")
    public ResponseEntity<ResponseUserProfileDto> findByNick(@PathVariable(name = "nickName") String nickName) {
        ResponseUserProfileDto dto = userFindService.findUserByNickName(nickName);
        return ResponseEntity.ok().body(dto);
    }
}
