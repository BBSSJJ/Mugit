package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.fixure.ProfileFixture;
import com.ssafy.mugit.fixure.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    @DisplayName("사용자 회원가입 테스트")
    void testUserRegist() {
        // given
        User tempUser = UserFixture.USER.getFixture();
        Profile profile = ProfileFixture.PROFILE.getFixture();

        // when
        tempUser.regist(profile);

        // then
        assertThat(tempUser.getProfile().getNickName())
                .isEqualTo("leaf");
        assertThat(tempUser.getProfile().getProfileText())
                .isEqualTo("프로필");
        assertThat(tempUser.getProfile().getProfileImagePath())
                .isEqualTo("http://localhost:8080/profile/1");
    }

    @Tag("login")
    @Test
    @DisplayName("[단위] 입력값 없을 때 기본값 설정 테스트")
    void defaultSettingTest() {
        // given
        User tempUser = UserFixture.USER.getFixture();
        Profile profile = ProfileFixture.NO_INPUT_PROFILE.getFixture();

        // when
        tempUser.regist(profile);

        // then
        assertThat(tempUser.getProfile().getNickName())
                .isEqualTo("leaf");
        assertThat(tempUser.getProfile().getProfileText())
                .isEqualTo("텍스트를 입력하세요.");
        assertThat(tempUser.getProfile().getProfileImagePath())
                .isEqualTo("https://mugit.site/files/008494eb-b272-4c83-919b-677378107fd2.jpg");
    }

    @Tag("regist")
    @Test
    @DisplayName("[단위] 회원가입 시 유저-프로필 동시 설정됨")
    void testRegistProfile() {
        // given
        User user = UserFixture.USER.getFixture();
        Profile profile = ProfileFixture.PROFILE.getFixture();

        // when
        user.regist(profile);

        // then
        assertThat(profile.getUser()).isEqualTo(user);
    }
}