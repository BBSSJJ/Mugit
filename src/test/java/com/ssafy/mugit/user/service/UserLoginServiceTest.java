package com.ssafy.mugit.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.mugit.global.api.OAuthApi;
import com.ssafy.mugit.user.dto.UserInfoDto;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.UserCookieUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.global.dto.DataKeys.LOGIN_USER_KEY;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static com.ssafy.mugit.fixure.UserInfoFixture.DEFAULT_GOOGLE_USER_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Tag("login")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserLoginServiceTest {
    @Mock
    OAuthApi oAuthApi;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    UserCookieUtil userCookieUtil;

    UserLoginService sut;

    @Mock
    FollowService followService;

    @BeforeEach
    void setUp() {
        mock();
        userCookieUtil = new UserCookieUtil();
        sut = new UserLoginService(oAuthApi, userRepository, followService, userCookieUtil);
        userRepository.save(USER.getFixture(PROFILE.getFixture()));
    }

    @Test
    @DisplayName("[통합] 로그인 완료 시 모든 로직 정상 호출 후 쿠키 반환")
    @Transactional
    void testAllLogicCalled() throws JsonProcessingException {
        // given
        String token = "valid_token";
        SnsType snsType = SnsType.GOOGLE;
        UserInfoDto userInfo = DEFAULT_GOOGLE_USER_INFO.getFixture();
        HttpSession session = new MockHttpSession();

        // when
        when(oAuthApi.getUserInfo(any(), any())).thenReturn(userInfo);
        HttpHeaders cookieHeader = sut.login(token, snsType, session);
        List<String> cookies = cookieHeader.get(SET_COOKIE);

        // then
        assertThat(cookies).contains(userCookieUtil.getTimeoutCookie("isLogined", "true").toString());
        assertThat(session.getAttribute(LOGIN_USER_KEY.getKey())).isNotNull();
    }
}