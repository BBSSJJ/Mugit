package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ssafy.mugit.user.fixture.ProfileFixture.DEFAULT_PROFILE;
import static com.ssafy.mugit.user.fixture.UserFixture.DEFAULT_LOGIN_USER;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("delete")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDeleteServiceTest {

    UserDeleteService sut;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        sut = new UserDeleteService(userRepository);
        User user = DEFAULT_LOGIN_USER.getFixture();
        user.regist(DEFAULT_PROFILE.getFixture());
        userRepository.save(user);
    }

    @Test
    @DisplayName("[통합] user와 연관된 모든 엔티티 삭제하는지 확인")
    @Transactional
    void testDeleteAllEntityRelatedToUser() {
        // given
        Long userId = DEFAULT_LOGIN_USER.getFixture().getId();
        
        // when
        sut.deleteUserEntity(userId);
        Optional<User> user = userRepository.findById(userId);

        // then
        assertThat(user.isEmpty()).isTrue();
    }
}