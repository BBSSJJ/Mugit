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
    USER_NOT_FOUND("해당 사용자를 찾을 수 없습니다."),
    NO_OAUTH_TOKEN("OAUTH 인증 토큰이 없습니다."),
    NOT_AUTHORIZED_USER("인증되지 않은 사용자입니다."),
    SELF_FOLLOW("본인을 팔로우했습니다."),
    ALREADY_FOLLOW("이미 팔로우한 계정입니다."),
    NOT_EXIST_FOLLOW("존재하지 않는 팔로우입니다."),
    SELF_PROFILE("본인의 프로필을 조회했습니다."),
    NOT_ALLOWED_ACCESS("접근 권한이 없습니다."),
    NOT_EXIST_READABLE_NOTIFICATION("읽을 수 있는 알림이 존재하지 않습니다."),
    NOTIFICATION_NOT_FOUNT("해당 알림을 찾을 수 없습니다."),
    DELETE_RECORD_NOT_IN_MUGITORY("뮤기토리에 없는 레코드를 삭제했습니다."),
    ALREADY_RECORDED_TO_MUGITORY("이미 뮤기토리에 등록된 레코드입니다."),
    NOT_EXIST_MUGITORY("뮤지토리가 없습니다.");

    private final String message;
}
