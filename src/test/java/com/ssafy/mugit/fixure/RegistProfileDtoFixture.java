package com.ssafy.mugit.fixure;

import com.ssafy.mugit.user.dto.request.RequestRegistProfileDto;

public enum RegistProfileDtoFixture {
    DEFAULT_REGIST_PROFILE_DTO("leaf", "안녕하세요.", "https://test.com/profile/1");

    private final String nickName;
    private final String profileText;
    private final String profileImage;

    RegistProfileDtoFixture(String nickName, String profileText, String profileImage) {
        this.nickName = nickName;
        this.profileText = profileText;
        this.profileImage = profileImage;
    }


    public RequestRegistProfileDto getRegistProfileDto() {
        return new RequestRegistProfileDto(nickName, profileText, profileImage);
    }
}
