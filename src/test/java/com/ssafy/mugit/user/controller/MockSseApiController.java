package com.ssafy.mugit.user.controller;

import com.ssafy.mugit.global.dto.MessageDto;
import com.ssafy.mugit.global.dto.SseMessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MockSseApiController {

    @PostMapping
    public ResponseEntity<MessageDto> getMessage(@RequestBody SseMessageDto<?> sseMessageDto) {
        return ResponseEntity.status(200).body(new MessageDto("알림 전송완료"));
    }
}
