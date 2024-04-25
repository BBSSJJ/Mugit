package com.ssafy.mugit.user.repository.querydsl;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;

public interface UserDtoRepository {
    ResponseUserProfileDto findUserProfileDtoByNickName(String nickName);
}
