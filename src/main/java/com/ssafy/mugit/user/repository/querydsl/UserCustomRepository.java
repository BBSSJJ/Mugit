package com.ssafy.mugit.user.repository.querydsl;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;

public interface UserCustomRepository {
    ResponseUserProfileDto findUserProfileDtoByNickName(String nickName);

    ResponseUserProfileDto findUserProfileDtoByUserId(Long userId);
}
