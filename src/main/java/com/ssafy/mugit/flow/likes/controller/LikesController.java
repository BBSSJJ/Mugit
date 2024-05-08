package com.ssafy.mugit.flow.likes.controller;

import com.ssafy.mugit.flow.likes.service.LikesService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@Tag(name = "Likes", description = "좋아요와 관련한 API입니다.")
public class LikesController {
    private final LikesService likesService;

    @Operation(summary = "Like 상태 변경", description = "좋아요를 이미 눌렀을 경우에는 취소, 누르지 않았을 때에는 좋아요 등록")
    @PatchMapping("/flows/{flowId}")
    ResponseEntity<MessageDto> updateLikes(@UserSession UserSessionDto user, @PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(new MessageDto(likesService.changeLikes(user.getId(), flowId)));
    }
}
