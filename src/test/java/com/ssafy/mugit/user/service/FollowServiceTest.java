package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.global.util.AcceptanceTestExecutionListener;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.notification.service.NotificationService;
import com.ssafy.mugit.user.dto.FollowerDto;
import com.ssafy.mugit.user.entity.Follow;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_EXIST_FOLLOW;
import static com.ssafy.mugit.global.exception.error.UserApiError.USER_NOT_FOUND;
import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_2;
import static com.ssafy.mugit.user.fixture.FollowerFixture.FOLLOWER_USER_3;
import static com.ssafy.mugit.user.fixture.ProfileFixture.*;
import static com.ssafy.mugit.user.fixture.UserFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("follow")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
class FollowServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Mock
    MessageBus messageBus;

    NotificationService notificationService;

    FollowService sut;

    User following;
    User followee;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService(messageBus, notificationRepository);
        sut = new FollowService(userRepository, followRepository, notificationService);

        following = USER.getFixture(PROFILE.getFixture());
        userRepository.save(following);

        followee = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(followee);
    }

    @Test
    @DisplayName("[통합] 존재하지 않는 유저 팔로우 시 오류")
    void testFollowNotRegistered() {
        // given
        Long followerId = following.getId();

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.follow(followerId, -1L));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException apiException = (UserApiException) exception;
        assertThat(apiException.getUserApiError()).isEqualTo(USER_NOT_FOUND);
    }

    @Test
    @Transactional
    @DisplayName("[통합] 한번 팔로우한 유저 다시 팔로우 시 오류")
    void testFollowAgain() {
        // given
        sut.follow(following.getId(), followee.getId());

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.follow(following.getId(), followee.getId()));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        assertThat(((UserApiException) exception).getUserApiError()).isEqualTo(UserApiError.ALREADY_FOLLOW);
    }

    @Test
    @DisplayName("[통합] 팔로우 후 DB에 정상 저장되는지 확인")
    void testFollowDB() {
        // given
        sut.follow(following.getId(), followee.getId());

        // when
        List<Follow> all = followRepository.findAll();
        Follow follow = all.get(0);

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(follow.getFollowee().getId()).isEqualTo(followee.getId());
        assertThat(follow.getFollower().getId()).isEqualTo(following.getId());
    }

    @Test
    @DisplayName("[통합] 본인이 팔로우하고 있는 유저 수 조회")
    void testMyTotalFollowerCount() {
        // given
        sut.follow(following.getId(), followee.getId());

        User followee2 = USER_3.getFixture();
        userRepository.save(followee2);
        sut.follow(following.getId(), followee2.getId());

        // when
        Long total = followRepository.countMyFollowers(following.getId());

        // then
        assertThat(total).isEqualTo(2L);
    }

    @Test
    @DisplayName("[통합] 본인을 팔로우하고 있는 유저 수 조회")
    void testMyTotalFollowingCount() {
        // given
        sut.follow(followee.getId(), following.getId());

        // when
        Long total = followRepository.countMyFollowings(following.getId());

        // then
        assertThat(total).isEqualTo(1L);
    }

    @Test
    @DisplayName("[통합] 본인이 팔로우하고 있는 모든 유저 조회")
    @Transactional
    void testMyTotalFollower() {
        // given
        sut.follow(following.getId(), followee.getId());

        User followee2 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(followee2);
        sut.follow(following.getId(), followee2.getId());

        // when
        List<FollowerDto> total = sut.getAllFollowers(following.getId());

        // then
        assertThat(total.size()).isEqualTo(2);
        assertThat(total)
                .anySatisfy(follower -> assertThat(follower).usingRecursiveComparison().ignoringFields("followerId").isEqualTo(FOLLOWER_USER_2.getFixture()))
                .anySatisfy(follower -> assertThat(follower).usingRecursiveComparison().ignoringFields("followerId").isEqualTo(FOLLOWER_USER_3.getFixture()));
    }

    @Test
    @DisplayName("[통합] 팔로우 정상삭제")
    void testDeleteFollow() {
        // given
        sut.follow(following.getId(), followee.getId());
        Long allFollowerCount = followRepository.countMyFollowings(followee.getId());
        assertThat(allFollowerCount).isEqualTo(1L);

        // when
        sut.unfollow(following.getId(), followee.getId());
        allFollowerCount = followRepository.countMyFollowings(followee.getId());

        // then
        assertThat(allFollowerCount).isEqualTo(0L);
    }

    @Test
    @DisplayName("[통합] 존재하지 않는 팔로우")
    void testDeleteFollowNotExist() {
        // given
        Long allFollowerCount = followRepository.countMyFollowers(following.getId());
        assertThat(allFollowerCount).isEqualTo(0L);

        // when
        Exception exception = assertThrows(Exception.class, () -> sut.unfollow(following.getId(), followee.getId()));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException apiException = (UserApiException) exception;
        assertThat(apiException.getUserApiError()).isEqualTo(NOT_EXIST_FOLLOW);
    }

    @Test
    @DisplayName("[통합] 프로필 팔로우여부 확인")
    void testFollowProfile() {
        // given
        User follower2 = USER_3.getFixture(PROFILE_3.getFixture());
        userRepository.save(follower2);

        // follower -> followee
        sut.follow(following.getId(), followee.getId());

        // follower2 -> follower
        sut.follow(follower2.getId(), following.getId());

        // when
        Boolean isFollower = sut.checkIsFollower(following.getId(), followee.getId());
        Boolean isFollowing = sut.checkIsFollower(following.getId(), follower2.getId());

        // then
        assertThat(isFollower).isTrue();
        assertThat(isFollowing).isFalse();
    }
}