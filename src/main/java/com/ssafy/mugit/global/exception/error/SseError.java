package com.ssafy.mugit.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SseError {
    SSE_EMITTER_NOT_FOUND("Emtter를 찾을 수 없습니다.");

    private final String message;
}
