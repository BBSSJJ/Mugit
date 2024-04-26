package com.ssafy.mugit.user.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestModifyUserInfoDtoTest {

    @Tag("profile")
    @Test
    @DisplayName("[단위] RequestModifyUserInfoDto 생성")
    void testCreateRequestModifyUserInfoDto() {
        // given
        String nickName = "leaf";
        String profileText = "프로필";
        String profileImagePath = "test/123";

        // when
        RequestModifyUserInfoDto dto = new RequestModifyUserInfoDto(nickName, profileText, profileImagePath);
        
        // then
        assertThat(dto.getNickName()).isEqualTo(nickName);
        assertThat(dto.getProfileText()).isEqualTo(profileText);
        assertThat(dto.getProfileImagePath()).isEqualTo(profileImagePath);
    }
}