package com.ssafy.mugit.global.exception;

import com.ssafy.mugit.global.exception.error.SseError;
import lombok.Getter;

@Getter
public class SseException extends RuntimeException {
    private final SseError sseError;
    private final String message;

    public SseException(SseError sseError) {
        this.sseError = sseError;
        this.message = sseError.getMessage();
    }
}
