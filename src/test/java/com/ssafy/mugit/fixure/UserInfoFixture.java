package com.ssafy.mugit.fixure;

import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;

public enum UserInfoFixture {
    DEFAULT_GOOGLE_USER_INFO("asdf1234", SnsType.GOOGLE, "test@test.com"),
    NOT_REGISTERED_USER_INFO("not registered sns id", SnsType.GOOGLE, "not_registered@test.com");

    private final String snsId;
    private final SnsType snsType;
    private final String email;

    UserInfoFixture(String snsId, SnsType snsType, String email) {
        this.snsId = snsId;
        this.snsType = snsType;
        this.email = email;
    }

    public UserInfoDto getFixture() {
        return new UserInfoDto(snsId, snsType, email);
    }
}
