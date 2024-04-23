package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;

public enum GoogleUserInfoFixture {
    DEFAULT_USER_INFO("asdf1234", SnsType.GOOGLE, "test@test.com"),
    NOT_REGISTERED_USER_INFO("not registered sns id", SnsType.GOOGLE, "not_registered@test.com"),;

    private final String snsId;
    private final SnsType snsType;
    private final String email;

    GoogleUserInfoFixture(String snsId, SnsType snsType, String email) {
        this.snsId = snsId;
        this.snsType = snsType;
        this.email = email;
    }

    public UserInfoDto getUserInfo() {
        return new UserInfoDto(snsId, snsType, email);
    }
}
