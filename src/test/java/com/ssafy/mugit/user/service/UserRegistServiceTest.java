package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.user.dto.request.RegistProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.RegistProfileDtoFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import com.ssafy.mugit.user.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpHeaders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("regist")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRegistServiceTest {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    CookieUtil cookieUtil;

    UserRegistService sut;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil(new JwtTokenUtil());
        sut = new UserRegistService(userRepository, profileRepository, cookieUtil);
    }

    @Test
    @DisplayName("[통합] 존재하는 닉네임일 경우 오류(409)")
    void testDuplicate() {
        // given
        profileRepository.save(ProfileFixture.DEFAULT_PROFILE.getProfile());
        RegistProfileDto registProfileDto = RegistProfileDtoFixture.DEFAULT_REGIST_PROFILE_DTO.getRegistProfileDto();

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.validateDuplicate(registProfileDto));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException userApiException = (UserApiException) exception;
        assertThat(userApiException.getUserApiError()).isEqualTo(UserApiError.DUPLICATE_NICK_NAME);
    }

    @Test
    @DisplayName("[통합] 로그인 시 임시로 생성한 유저에 프로필 설정(repo)")
    void testRegistProfileInTempUser() {
        // given
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        Profile profile = ProfileFixture.DEFAULT_PROFILE.getProfile();

        // when
        sut.regist(user, profile);
        Profile profileInDB = profileRepository.getReferenceById(profile.getId());
        User userInDB = userRepository.getReferenceById(user.getId());

        // then
        assertThat(profileInDB).isNotNull();
        assertThat(userInDB).isNotNull();
        assertThat(profileInDB.getUser()).isEqualTo(userInDB);
        assertThat(userInDB.getProfile()).isEqualTo(profileInDB);
    }
    
    @Test
    @DisplayName("[통합] Header에 로그인 관련 쿠키 설정(cookie)")
    void testSetLoginCookie() {
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
}