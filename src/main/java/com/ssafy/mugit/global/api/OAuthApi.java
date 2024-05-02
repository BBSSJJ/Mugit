package com.ssafy.mugit.global.api;

import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;

public interface OAuthApi {
    UserInfoDto getUserInfo(String token, SnsType snsType);
}
