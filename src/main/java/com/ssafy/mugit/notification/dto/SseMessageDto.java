package com.ssafy.mugit.notification.dto;

import com.ssafy.mugit.global.dto.SseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SseMessageDto<T> {
    private Long userId;
    private SseEvent event;
    private T message;
}
