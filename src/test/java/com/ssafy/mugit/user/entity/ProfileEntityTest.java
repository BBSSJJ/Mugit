package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileEntityTest {

    @Tag("regist")
    @Test
    @DisplayName("[단위] 입력값이 없으면 기본값이 설정됨")
    void testDefaultSetting() {
        // given
        Profile profile = ProfileFixture.NO_INPUT_PROFILE.getFixture();
        User user = UserFixture.USER.getFixture();

        // when
        user.regist(profile);
        
        // then
        assertThat(profile.getProfileText()).isEqualTo("텍스트를 입력하세요.");
        assertThat(profile.getProfileImagePath()).isEqualTo("DEFAULT_IMAGE_URL");
    }
}