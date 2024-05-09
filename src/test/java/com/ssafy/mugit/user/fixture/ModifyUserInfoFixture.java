package com.ssafy.mugit.user.fixture;

import com.ssafy.mugit.user.dto.request.RequestModifyUserInfoDto;

public enum ModifyUserInfoFixture {
    MODIFY_USER_INFO_DTO_01("leaf2", "변경된 프로필", "변경된 사진경로"),DUPLICATE_MODIFY_USER_INFO_DTO("leaf", "변경된 프로필", "변경된 사진경로"),
    MODIFY_USER_INFO_WITH_BLANK("leaf2", "", "");

    private final String nickName;
    private final String profileText;
    private final String profileImagePath;

    ModifyUserInfoFixture(final String nickName, final String profileText, final String profileImagePath) {
        this.nickName = nickName;
        this.profileText = profileText;
        this.profileImagePath = profileImagePath;
    }

    public RequestModifyUserInfoDto getFixture() {
        return new RequestModifyUserInfoDto(nickName, profileText, profileImagePath);
    }
}
