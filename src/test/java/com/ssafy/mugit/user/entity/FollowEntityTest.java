package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("follow")
class FollowEntityTest {

    @Test
    @DisplayName("[단위] 팔로우 엔티티 생성")
    void testCreateFollowEntity() {
        // given
        User follower = USER.getFixture();
        User followee = USER_2.getFixture();

        // when
        Follow follow = new Follow(follower, followee);

        // then
        assertThat(follow).isNotNull();
        assertThat(follow.getFollower()).isInstanceOf(User.class);
        assertThat(follow.getFollowing()).isInstanceOf(User.class);
    }

    @Test
    @DisplayName("[단위] 본인을 팔로우 시 오류")
    void testSelfFollow() {
        // given
        User follower = USER.getFixture();
        User followee = USER.getFixture();

        // when
        Exception exception = assertThrows(Exception.class, () -> new Follow(follower, followee));

        // then
        assertThat(exception).isInstanceOf(UserApiException.class);
        UserApiException apiException = (UserApiException) exception;
        assertThat(apiException.getUserApiError()).isEqualTo(UserApiError.SELF_FOLLOW);
    }
}