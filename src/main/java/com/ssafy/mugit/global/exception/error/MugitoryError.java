package com.ssafy.mugit.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MugitoryError {
    DELETE_ALL_RECORD_IN_MUGITORY("뮤지토리의 모든 레코드를 삭제했습니다."),
    DELETE_RECORD_NOT_IN_MUGITORY("뮤지토리에 없는 레코드를 삭제했습니다."),
    FIRST_RECORD_TODAY("오늘 첫 레코드입니다.");
    private final String message;
}
