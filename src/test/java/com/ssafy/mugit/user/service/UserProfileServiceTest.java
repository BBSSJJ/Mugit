package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.user.dto.request.RequestModifyUserInfoDto;
import com.ssafy.mugit.user.dto.response.ResponseUserProfileDto;
import com.ssafy.mugit.user.entity.Profile;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.fixture.ModifyUserInfoFixture;
import com.ssafy.mugit.user.repository.ProfileRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.user.fixture.ModifyUserInfoFixture.DUPLICATE_MODIFY_USER_INFO_DTO;
import static com.ssafy.mugit.user.fixture.ProfileFixture.*;
import static com.ssafy.mugit.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class UserProfileServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    FollowService followService;

    UserProfileService sut;

    @BeforeEach
    void setUp() {
        sut = new UserProfileService(userRepository, profileRepository, followService);
    }


    @Tag("profile")
    @Test
    @DisplayName("[통합] 유저 PK로 ResponseProfileDto 생성")
    void testCreateUserProfileDto() {
        // given
        User user = USER.getFixture();
        Profile profile = PROFILE.getFixture();
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

    @Tag("profile")
    @Test
    @Transactional
    @DisplayName("[통합] ProfileRepository 활용 Profile 정보 수정")
    void testModifyProfile() {
        // given
        User user = USER.getFixture();
        Profile profile = PROFILE.getFixture();
        user.regist(profile);
        userRepository.save(user);

        Long userId = user.getId();
        RequestModifyUserInfoDto dto = ModifyUserInfoFixture.DEFAULT_MODIFY_USER_INFO_DTO.getFixture();

        // when
        sut.updateProfile(userId, dto);
        Profile profileInDB = profileRepository.findByUserId(userId);

        // then
        assertThat(profileInDB).isNotNull();
        assertThat(profileInDB.getNickName()).isEqualTo(dto.getNickName());
        assertThat(profileInDB.getProfileText()).isEqualTo(dto.getProfileText());
        assertThat(profileInDB.getProfileImagePath()).isEqualTo(dto.getProfileImagePath());
    }

    @Tag("profile")
    @Test
    @Transactional
    @DisplayName("[통합] 중복 프로필 수정 시 오류")
    void testModifyProfileDuplication() {
        // given
        User user = USER.getFixture();
        Profile profile = PROFILE.getFixture();
        user.regist(profile);
        userRepository.save(user);

        Long userId = user.getId();
        RequestModifyUserInfoDto dto = DUPLICATE_MODIFY_USER_INFO_DTO.getFixture();

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.updateProfile(userId, dto));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException userApiException = (UserApiException) exception;
        assertThat(userApiException.getUserApiError()).isEqualTo(UserApiError.DUPLICATE_NICK_NAME);
    }

    @Tag("follow")
    @Test
    @DisplayName("[통합] 내가 팔로우중인지 / 나를 팔로우중인지 확인")
    void testFollow() {
        // given
        User user = USER.getFixture(PROFILE.getFixture());
        User follower = USER_2.getFixture(PROFILE_2.getFixture());
        User following = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(user);
        userRepository.save(follower);
        userRepository.save(following);

        // 유저 1 -> 유저 2
        followService.follow(user.getId(), follower.getId());

        // 유저 3 -> 유저 1
        followService.follow(following.getId(), user.getId());

        // when
        ResponseUserProfileDto followerProfile = sut.getProfileById(user.getId(), follower.getId());
        ResponseUserProfileDto followingProfile = sut.getProfileById(user.getId(), following.getId());

        // then
        assertThat(followerProfile).hasFieldOrPropertyWithValue("isFollower", true);
        assertThat(followerProfile).hasFieldOrPropertyWithValue("isFollowing", false);
        assertThat(followingProfile).hasFieldOrPropertyWithValue("isFollower", false);
        assertThat(followingProfile).hasFieldOrPropertyWithValue("isFollowing", true);
    }

    @Test
    @DisplayName("[통합] 본인 프로필 조회")
    void testMyProfile() {
        // given
        User user = USER.getFixture(PROFILE.getFixture());
        userRepository.save(user);

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.getProfileById(user.getId(), user.getId()));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException userApiException = (UserApiException) exception;
        assertThat(userApiException.getUserApiError()).isEqualTo(UserApiError.SELF_PROFILE);
    }
}