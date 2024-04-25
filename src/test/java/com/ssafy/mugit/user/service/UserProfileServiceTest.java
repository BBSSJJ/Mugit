package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.UserFixture;
import com.ssafy.mugit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserProfileServiceTest {

    @Autowired
    UserRepository userRepository;

    UserProfileService sut;

    @BeforeEach
    void setUp() {
        sut = new UserProfileService(userRepository);
    }

    @Test
    @DisplayName("[통합] 유저 PK로 ResponseProfileDto 생성")
    void testCreateUserProfileDto() {
        // given
        User user = UserFixture.DEFAULT_LOGIN_USER.getUser();
        Profile profile = ProfileFixture.DEFAULT_PROFILE.getProfile();
        user.regist(profile);
        userRepository.save(user);

        Long userId = 1L;
        
        // when
        ResponseUserProfileDto userDto = sut.getProfileById(userId);
        
        // then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getNickName()).isEqualTo(profile.getNickName());
    }
}