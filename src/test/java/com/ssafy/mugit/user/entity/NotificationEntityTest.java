package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.ssafy.mugit.notification.entity.NotificationType.FOLLOW;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;

class NotificationEntityTest {

    @Test
    @DisplayName("[단위] Follow 알림 생성")
    void testCreateFollowNotification() {
        // given
        User followeeUser = USER.getFixture(PROFILE.getFixture());
        User followingUser = USER_2.getFixture(PROFILE_2.getFixture());

        // when
        Notification notification = new Notification(followeeUser, followingUser, followeeUser.getId(), followingUser.getClass(), FOLLOW);

        // then
        assertThat(notification.getNotifier().getId()).isEqualTo(followeeUser.getId());
        assertThat(notification.getNotified().getId()).isEqualTo(followingUser.getId());
        assertThat(notification.getType()).isEqualTo(FOLLOW);
        assertThat(notification.getCauseEntityId()).isEqualTo(followeeUser.getId());
        assertThat(notification.getIsRead()).isFalse();
        assertThat(notification.getDescription()).isEqualTo(followingUser.getProfile().getNickName() + "님이 당신을 팔로우합니다.");
    }

}