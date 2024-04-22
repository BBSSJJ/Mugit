package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.web.api.OAuthWebClientApi;
import com.ssafy.mugit.global.web.dto.UserInfoDto;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import com.ssafy.mugit.user.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserLoginServiceTest {
    @Mock
    OAuthWebClientApi oAuthWebClientApi;

    @Autowired
    UserRepository userRepository;

    CookieUtil cookieUtil;

    UserLoginService sut;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil(new JwtTokenUtil());
        sut = new UserLoginService(oAuthWebClientApi, userRepository, cookieUtil);
    }

    @Test
    @DisplayName("[단위] 토큰에 Bearer 빼기")
    void testExtractBearer() {
        // given
        String token = "Bearer asdf1234";

        // when
        String bearerToken = sut.getBearerToken(token);

        // then
        assertThat(bearerToken).isEqualTo("asdf1234");
    }

    @Test
    @DisplayName("[통합] 로그인 시 받은 토큰으로 유저정보 받아옴(OAuth)")
    void testGetUserInfoByToken() {
        // given
        String token = "qwerasdf1234";
        SnsType snsType = SnsType.GOOGLE;
        UserInfoDto userInfo = new UserInfoDto("asdf1234", SnsType.GOOGLE, "test@test.com");
        given(oAuthWebClientApi.getUserInfo(token, snsType)).willReturn(userInfo);

        // when
        UserInfoDto userInfoByToken = sut.getUserInfo(token, snsType);

        // then
        assertThat(userInfoByToken).isEqualTo(userInfo);
    }

    @Test
    @DisplayName("[통합] 유저정보를 통해 유저 엔티티 생성(repo)")
    void testGetUserByUserInfo() {
        // given
        userRepository.save(UserFixture.DEFAULT_LOGIN_USER.getUser());
        UserInfoDto userInfo = new UserInfoDto("asdf1234", SnsType.GOOGLE, "teset@test.com");

        // when
        User user = sut.getUser(userInfo);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@test.com");
        assertThat(user.getSnsId()).isEqualTo("asdf1234");
    }

    @Test
    @DisplayName("[통합] 유저정보가 등록되지 않았으면 error 반환(repo)")
    void testRedirectRegist() {
        // given
        UserInfoDto userInfo = new UserInfoDto("zxcv1234", SnsType.GOOGLE, "teset@test.com");

        // when
        assertThatThrownBy(() -> sut.getUser(userInfo))
                .isInstanceOf(UserApiException.class);
        UserApiException userApiException = assertThrows(UserApiException.class, () -> sut.getUser(userInfo));

        // then
        assertThat(userApiException.getUserApiError()).isEqualTo(UserApiError.NOT_REGISTERED_USER);
    }

    @Test
    @DisplayName("[통합] 회원가입 이동 시 회원가입 Cookie 설정(cookie)")
    void testCookieSetting() {
        // given
        Profile profile = ProfileFixture.DEFAULT_PROFILE.getProfile();
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser(profile);

        // when
        HttpHeaders cookieHeader = sut.getLoginCookieHeader(user);
        List<String> cookies = cookieHeader.get(HttpHeaders.SET_COOKIE);

        // then
        assertThat(cookies).contains(cookieUtil.getUserInfoCookie("isLogined", "true").toString());
        assertThat(cookies)
                .anyMatch(cookie -> cookie.contains("isLogined=true"))
                .anyMatch(cookie -> cookie.contains("nickName=leaf"))
                .anyMatch(cookie -> cookie.contains("profileText=%ED%94%84%EB%A1%9C%ED%95%84"))
                .anyMatch(cookie -> cookie.contains("profileImage=http%3A%2F%2Flocalhost%3A8080%2Fprofile%2F1"));
    }
    
    @Test
    @DisplayName("[통합] 로그인 완료 시 모든 로직 정상 호출 후 쿠키 반환")
    void testAllLogicCalled() {
        // given
        String token = "qwerasdf1234";
        SnsType snsType = SnsType.GOOGLE;
        UserInfoDto userInfo = new UserInfoDto("asdf1234", SnsType.GOOGLE, "test@test.com");
        given(oAuthWebClientApi.getUserInfo(token, snsType)).willReturn(userInfo);
        userRepository.save(UserFixture.DEFAULT_LOGIN_USER.getUser(ProfileFixture.DEFAULT_PROFILE.getProfile()));

        // when
        HttpHeaders cookieHeader = sut.loginAndGetCookieHeader(token, snsType);
        List<String> cookies = cookieHeader.get(HttpHeaders.SET_COOKIE);
        
        // then
        assertThat(cookies).contains(cookieUtil.getUserInfoCookie("isLogined", "true").toString());
    }
}