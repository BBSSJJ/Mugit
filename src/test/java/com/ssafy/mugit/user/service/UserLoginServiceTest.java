package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.mugit.auth.SessionKeys;
import com.ssafy.mugit.global.web.api.OAuthApi;
import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.fixture.UserInfoFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@Tag("login")
@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserLoginServiceTest {
    @Mock
    OAuthApi oAuthApi;

    @Autowired
    UserRepository userRepository;

    CookieUtil cookieUtil;

    UserLoginService sut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cookieUtil = new CookieUtil();
        sut = new UserLoginService(oAuthApi, userRepository, cookieUtil);
    }

    @Test
    @DisplayName("[통합] 로그인 완료 시 모든 로직 정상 호출 후 쿠키 반환")
    void testAllLogicCalled() throws JsonProcessingException {
        // given
        String token = "qwerasdf1234";
        SnsType snsType = SnsType.GOOGLE;
        UserInfoDto userInfo = UserInfoFixture.DEFAULT_GOOGLE_USER_INFO.getFixture();
        userRepository.save(UserFixture.DEFAULT_LOGIN_USER.getFixture(ProfileFixture.DEFAULT_PROFILE.getFixture()));
        HttpSession session = new MockHttpSession();

        // when
        when(oAuthApi.getUserInfo(any(), any())).thenReturn(userInfo);
        HttpHeaders cookieHeader = sut.login(token, snsType, session);
        List<String> cookies = cookieHeader.get(HttpHeaders.SET_COOKIE);

        // then
        assertThat(cookies).contains(cookieUtil.getTimeoutCookie("isLogined", "true").toString());
        assertThat(session.getAttribute(SessionKeys.LOGIN_USER_KEY.getKey())).isNotNull();
    }
}