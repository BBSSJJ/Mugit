package com.ssafy.mugit.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserApiError {
    ILLEGAL_SNS_TYPE("등록되지 않은 sns 타입입니다."),
    UNAUTHORIZED_BY_OAUTH("OAUTH 인증에 실패했습니다."),
    DUPLICATE_NICK_NAME("중복되는 닉네임입니다."),
    NOT_REGISTERED_USER("등록되지 않은 사용자입니다."),
    NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
    NO_OAUTH_TOKEN("OAUTH 인증 토큰이 없습니다."),
    NOT_AUTHORIZED_USER("인증되지 않은 사용자입니다.");

    private final String message;
}
