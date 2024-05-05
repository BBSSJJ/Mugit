package com.ssafy.mugit.flow.likes.controller;

import com.ssafy.mugit.flow.likes.service.LikesService;
import com.ssafy.mugit.global.config.UserSession;
import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PatchMapping("/flows/{flowId}")
    ResponseEntity<MessageDto> updateLikes(@UserSession UserSessionDto user, @PathVariable("flowId") Long flowId) {
        return ResponseEntity.status(200).body(new MessageDto(likesService.changeLikes(user.getId(), flowId)));
    }
}
