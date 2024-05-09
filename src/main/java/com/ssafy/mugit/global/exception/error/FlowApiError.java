package com.ssafy.mugit.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FlowApiError {
    NOT_ALLOWED_ACCESS("허용되지 않은 접근"),
    NOT_RELEASED_FLOW("릴리즈 되지 않은 플로우"),
    NO_RECORD("레코드가 없음"),
    ALREADY_RELEASED_FLOW("이미 릴리즈된 플로우"),
    NOT_EXIST_FLOW("존재하지 않은 플로우"),
    NO_CONTENT("빈 내용"),
    NO_MUSIC("음악 파일이 없음");

    private final String message;
}
