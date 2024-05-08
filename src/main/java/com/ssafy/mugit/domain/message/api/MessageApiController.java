package com.ssafy.mugit.domain.message.api;

import com.ssafy.mugit.infrastructure.dto.SseMessageDto;
import com.ssafy.mugit.domain.message.service.MessageService;
import com.ssafy.mugit.presentation.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageApiController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDto> getMessage(@RequestBody SseMessageDto<?> sseMessageDto) {
        messageService.send(sseMessageDto);
        return ResponseEntity.status(200).body(new MessageDto("알림 전송완료"));
    }
}
