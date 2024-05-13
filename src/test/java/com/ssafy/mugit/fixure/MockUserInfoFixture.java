package com.ssafy.mugit.fixure;

import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;

public enum MockUserInfoFixture {
    MOCK_USER_INFO(SnsType.MOCK, "mock@test.com", "test_mock_nickname", "테스트 프로필", "test/1234");
    private final SnsType snsType;
    private final String email;
    private final String nickName;
    private final String profileText;
    private final String profileImagePath;

    MockUserInfoFixture(SnsType snsType, String email, String nickName, String profileText, String profileImagePath) {
        this.snsType = snsType;
        this.email = email;
        this.nickName = nickName;
        this.profileText = profileText;
        this.profileImagePath = profileImagePath;
    }

    public MockUserInfoDto getUserInfo() {
        return new MockUserInfoDto(snsType, email, nickName, profileText, profileImagePath);
    }

}
