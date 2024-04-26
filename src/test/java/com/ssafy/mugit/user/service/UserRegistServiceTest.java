package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.user.dto.request.RequestRegistProfileDto;
import com.ssafy.mugit.user.fixture.ProfileFixture;
import com.ssafy.mugit.user.fixture.RegistProfileDtoFixture;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.util.CookieUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("regist")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserRegistServiceTest {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    CookieUtil cookieUtil;

    UserRegistService sut;

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();
        sut = new UserRegistService(userRepository, profileRepository, cookieUtil);
    }

    @Test
    @DisplayName("[통합] 존재하는 닉네임일 경우 오류(409)")
    void testDuplicate() {
        // given
        profileRepository.save(ProfileFixture.DEFAULT_PROFILE.getFixture());
        RequestRegistProfileDto requestRegistProfileDto = RegistProfileDtoFixture.DEFAULT_REGIST_PROFILE_DTO.getRegistProfileDto();

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.validateDuplicate(requestRegistProfileDto));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException userApiException = (UserApiException) exception;
        assertThat(userApiException.getUserApiError()).isEqualTo(UserApiError.DUPLICATE_NICK_NAME);
    }
}