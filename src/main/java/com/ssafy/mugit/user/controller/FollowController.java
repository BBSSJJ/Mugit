package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.web.dto.ListDto;
import com.ssafy.mugit.global.web.dto.MessageDto;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.service.FollowService;
import com.ssafy.mugit.user.dto.UserSessionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User", description = "유저와 관련한 API입니다.")
public class FollowController {
    private final FollowService followService;

    @Operation(summary = "팔로우", description = "타인을 팔로우한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "팔로우 완료", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "400", description = "본인 팔로우 시도", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = MessageDto.class))),
            @ApiResponse(responseCode = "404", description = "해당 사용자 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))})
    @PostMapping("/{id}/follows")
    public ResponseEntity<MessageDto> follow(
            @PathVariable(name = "id") Long followeeId,
            HttpSession session) {

        // Session에서 본인 ID 찾아서 Follow
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        followService.follow(userInSession.getId(), followeeId);

        return ResponseEntity.status(201).body(new MessageDto("팔로우 완료"));
    }

    @Operation(summary = "팔로워 조회", description = "팔로워들을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 조회 완료", content = @Content(schema = @Schema(implementation = FollowerDto.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))})
    @GetMapping("/followers")
    public ResponseEntity<ListDto<List<FollowerDto>>> followers(HttpSession session) {

        // Session에서 본인 ID 찾기
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        List<FollowerDto> allFollower = followService.getAllFollower(userInSession.getId());

        return ResponseEntity.status(200).body(new ListDto<>(allFollower));
    }

    @Operation(summary = "팔로잉 조회", description = "팔로잉들을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로잉 조회 완료", content = @Content(schema = @Schema(implementation = FollowerDto.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))})
    @GetMapping("/followings")
    public ResponseEntity<ListDto<List<FollowerDto>>> followings(HttpSession session) {

        // Session에서 본인 ID 찾기
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        List<FollowerDto> allFollower = followService.getAllFollowings(userInSession.getId());

        return ResponseEntity.status(200).body(new ListDto<>(allFollower));
    }

    @Operation(summary = "언팔로우", description = "팔로우를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "언팔로우 완료", content = @Content(schema = @Schema(implementation = FollowerDto.class))),
            @ApiResponse(responseCode = "204", description = "이미 삭제된 팔로우", content = @Content(schema = @Schema(implementation = FollowerDto.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = MessageDto.class)))})
    @DeleteMapping("{id}/follows")
    public ResponseEntity<MessageDto> unfollow(
            @PathVariable(name = "id") Long followerId, HttpSession session) {

        // Session에서 본인 ID 찾기
        UserSessionDto userInSession = (UserSessionDto) session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey());
        followService.unfollow(userInSession.getId(), followerId);

        return ResponseEntity.status(200).body(new MessageDto("언팔로우 완료"));
    }
}
