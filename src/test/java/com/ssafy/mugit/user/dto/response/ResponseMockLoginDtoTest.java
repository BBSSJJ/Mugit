package com.ssafy.mugit.user.dto.response;

import com.ssafy.mugit.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import java.util.Base64;

import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.UserFixture.USER_WITH_PK;
import static org.assertj.core.api.Assertions.assertThat;

class ResponseMockLoginDtoTest {

    @Test
    @DisplayName("쿠키로 가짜 로그인 정보 넣기")
    void testInsertMockUserLoginDtoByCookie() {
        // given
        User user = USER_WITH_PK.getFixture(PROFILE.getFixture());
        MockHttpSession session = new MockHttpSession();

        // when
        ResponseMockLoginDto responseMockLoginDto = new ResponseMockLoginDto(user, session.getId(), 1234L, 3241L);

        // then
        assertThat(responseMockLoginDto).isNotNull();
        assertThat(responseMockLoginDto.getSessionId()).isEqualTo(Base64.getEncoder().encodeToString(session.getId().getBytes()));
        assertThat(responseMockLoginDto.getUserId()).isEqualTo(1L);
        assertThat(responseMockLoginDto.getNickName()).isEqualTo("leaf");
        assertThat(responseMockLoginDto.getProfileText()).isEqualTo("프로필");
        assertThat(responseMockLoginDto.getProfileImagePath()).isEqualTo("http://localhost:8080/profile/1");
        assertThat(responseMockLoginDto.getFollowers()).isEqualTo(1234L);
        assertThat(responseMockLoginDto.getFollowings()).isEqualTo(3241L);
    }
}