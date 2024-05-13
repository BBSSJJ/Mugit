package com.ssafy.mugit.user.repository;

import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.fixure.ProfileFixture;
import com.ssafy.mugit.fixure.UserFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    UserRepository sut;

    @Test
    @DisplayName("User + Profile dto 조회 테스트")
    void testGetUserProfileDto() {
        // given
        User user = UserFixture.USER.getFixture();
        user.regist(ProfileFixture.PROFILE.getFixture());
        sut.save(user);
        String nickName = "leaf";

        // when
        ResponseUserProfileDto userDto = sut.findUserProfileDtoByNickName(nickName);

        // then
        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getNickName()).isEqualTo(nickName);
    }
}