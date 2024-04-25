package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.dto.MockUserInfoDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.SnsType;
import com.ssafy.mugit.user.fixture.MockUserInfoFixture;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("mock")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MockUserFindServiceTest {

    MockUserService sut;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @BeforeEach
    void setUp() {
        sut = new MockUserService(userRepository, profileRepository, new CookieUtil());
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
}