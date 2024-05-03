package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.Follow;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
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

import java.util.List;

import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_EXIST_FOLLOW;
import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_FOUND;
import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_2;
import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_3;
import static com.ssafy.mugit.user.fixture.ProfileFixture.*;
import static com.ssafy.mugit.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("follow")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class FollowServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    NotificationService notificationService;

    FollowService sut;

    @BeforeEach
    void setUp() {
        sut = new FollowService(userRepository, followRepository, notificationService);

        User user = USER.getFixture(PROFILE.getFixture());
        userRepository.save(user);

        User user2 = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(user2);
    }

    @Test
    @DisplayName("[통합] 존재하지 않는 유저 팔로우 시 오류")
    void testFollowNotRegistered() {
        // given
        User me = USER.getFixture();

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.follow(me.getId(), -1L));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException apiException = (UserApiException) exception;
        assertThat(apiException.getUserApiError()).isEqualTo(NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("[통합] 한번 팔로우한 유저 다시 팔로우 시 오류")
    void testFollowAgain() {
        // given
        User follower = USER.getFixture();
        User followee = USER_2.getFixture();

        sut.follow(follower.getId(), followee.getId());

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.follow(follower.getId(), followee.getId()));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        assertThat(((UserApiException) exception).getUserApiError()).isEqualTo(UserApiError.ALREADY_FOLLOW);
    }

    @Test
    @DisplayName("[통합] 팔로우 후 DB에 정상 저장되는지 확인")
    void testFollowDB() {
        // given
        User follower = USER.getFixture();
        User followee = USER_2.getFixture();
        // when
        sut.follow(follower.getId(), followee.getId());
        List<Follow> all = followRepository.findAll();
        Follow follow = all.get(0);

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(follow.getFollower().getId()).isEqualTo(follower.getId());
        assertThat(follow.getFollowing().getId()).isEqualTo(followee.getId());
    }

    @Test
    @DisplayName("[통합] 본인이 팔로우하고 있는 유저 수 조회")
    void testMyTotalFollowerCount() {
        // given
        long myId = USER.getFixture().getId();

        long followerId = USER_2.getFixture().getId();
        sut.follow(myId, followerId);

        User user3 = USER_3.getFixture();
        userRepository.save(user3);
        sut.follow(myId, user3.getId());

        // when
        Long total = sut.getAllFollowerCount(myId);

        // then
        assertThat(total).isEqualTo(2L);
    }

    @Test
    @DisplayName("[통합] 본인을 팔로우하고 있는 유저 수 조회")
    void testMyTotalFollowingCount() {
        // given
        long myId = USER.getFixture().getId();

        long followerId = USER_2.getFixture().getId();
        sut.follow(followerId, myId);

        // when
        Long total = sut.getAllFollowingCount(myId);

        // then
        assertThat(total).isEqualTo(1L);
    }

    @Test
    @DisplayName("[통합] 본인이 팔로우하고 있는 모든 유저 조회")
    @Transactional
    void testMyTotalFollower() {
        // given
        long myId = USER.getFixture().getId();
        long followerId = USER_2.getFixture().getId();

        sut.follow(myId, followerId);

        User user3 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(user3);
        sut.follow(myId, user3.getId());

        // when
        List<FollowerDto> total = sut.getAllFollower(myId);

        // then
        assertThat(total.size()).isEqualTo(2);
        assertThat(total)
                .anyMatch(follower -> follower.equals(FOLLOWER_USER_2.getFixture()))
                .anyMatch(follower -> follower.equals(FOLLOWER_USER_3.getFixture()));
    }

    @Test
    @DisplayName("[통합] 팔로우 정상삭제")
    void testDeleteFollow() {
        // given
        User me = USER.getFixture(PROFILE.getFixture());
        User follower = USER_2.getFixture(PROFILE_2.getFixture());

        sut.follow(me.getId(), follower.getId());
        Long allFollowerCount = sut.getAllFollowerCount(me.getId());
        assertThat(allFollowerCount).isEqualTo(1L);

        // when
        sut.unfollow(me.getId(), follower.getId());
        allFollowerCount = sut.getAllFollowerCount(me.getId());

        // then
        assertThat(allFollowerCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("[통합] 존재하지 않는 팔로우")
    void testDeleteFollowNotExist() {
        // given
        User me = USER.getFixture(PROFILE.getFixture());
        User follower = USER_2.getFixture(PROFILE_2.getFixture());

        Long allFollowerCount = sut.getAllFollowerCount(me.getId());
        assertThat(allFollowerCount).isEqualTo(0L);

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.unfollow(me.getId(), follower.getId()));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException apiException = (UserApiException) exception;
        assertThat(apiException.getUserApiError()).isEqualTo(NOT_EXIST_FOLLOW);
    }

    @Test
    @DisplayName("[통합] 프로필 팔로우여부 확인")
    void testFollowProfile() {
        // given
        User me = USER.getFixture(PROFILE.getFixture());
        User follower = USER_2.getFixture(PROFILE_2.getFixture());
        User following = USER_3.getFixture(PROFILE_3.getFixture());

        userRepository.save(following);
        sut.follow(me.getId(), follower.getId());
        sut.follow(following.getId(), me.getId());

        // when
        Boolean isFollower = sut.checkIsFollower(me.getId(), follower.getId());
        Boolean isFollowing = sut.checkIsFollower(me.getId(), following.getId());

        // then
        assertThat(isFollower).isTrue();
        assertThat(isFollowing).isFalse();
    }
}