package com.ssafy.mugit.global.dto;

import com.ssafy.mugit.global.entity.SseEvent;
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
