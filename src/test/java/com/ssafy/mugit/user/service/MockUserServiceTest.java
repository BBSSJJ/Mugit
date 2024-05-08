package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.dto.response.ResponseMockLoginDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.fixture.MockUserInfoFixture;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.UserCookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;

import java.util.Base64;

import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_WITH_PK;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("mock")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MockUserServiceTest {

    MockUserService sut;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        sut = new MockUserService(userRepository, profileRepository, followRepository, new UserCookieUtil());
    }

    @Test
    @DisplayName("[단위] 테스트용 계정 정상 생성")
    void testCreateUserByUserInfo() {
        // given
        MockUserInfoDto userInfo = MockUserInfoFixture.MOCK_USER_INFO.getUserInfo();
        
        // when
        User createdUser = sut.createUserByUserInfo(userInfo);

        // then
        assertThat(createdUser.getSnsType()).isEqualTo(SnsType.MOCK);
        assertThat(createdUser.getEmail()).isEqualTo("mock@test.com");
        assertThat(createdUser.getSnsId()).isEqualTo("Mock_" + createdUser.getId());
    }

    @Test
    @DisplayName("[통합] 테스트용 계정 정상 저장")
    void testSaveMockUser() {
        // given
        MockUserInfoDto userInfo = MockUserInfoFixture.MOCK_USER_INFO.getUserInfo();

        // when
        User createdUser = sut.createUserByUserInfo(userInfo);
        User userInDB = userRepository.getReferenceById(createdUser.getId());

        // then
        assertThat(userInDB).isNotNull();
        assertThat(userInDB).isEqualTo(createdUser);

    }
    
    @Test
    @DisplayName("[통합] 가짜 로그인 시 JSESSIONID와 쿠키에 저장되는 정보 함께 Body에 전송하기")
    void testSendMockLoginDto() {
        // given
        User user = USER.getFixture(PROFILE.getFixture());
        userRepository.save(user);
        MockHttpSession httpSession = new MockHttpSession();
        String encodedSessionId = Base64.getEncoder().encodeToString(httpSession.getId().getBytes());

        // when
        Pair<HttpHeaders, ResponseMockLoginDto> loginHeaderAndUserInfoDto = sut.login(user.getId(), httpSession);
        ResponseMockLoginDto userInfoDto = loginHeaderAndUserInfoDto.getSecond();

        // then
        assertThat(userInfoDto).isNotNull();
        assertThat(userInfoDto.getSessionId()).isEqualTo(encodedSessionId);
        assertThat(userInfoDto.getUserId()).isEqualTo(user.getId());
        assertThat(userInfoDto.getNickName()).isEqualTo(user.getProfile().getNickName());
        assertThat(userInfoDto.getProfileText()).isEqualTo(user.getProfile().getProfileText());
        assertThat(userInfoDto.getProfileImagePath()).isEqualTo(user.getProfile().getProfileImagePath());
        assertThat(userInfoDto.getFollowers()).isEqualTo(0);
        assertThat(userInfoDto.getFollowings()).isEqualTo(0);
    }
}