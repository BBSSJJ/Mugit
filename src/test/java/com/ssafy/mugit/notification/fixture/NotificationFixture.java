package com.ssafy.mugit.notification.fixture;

import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.NotificationType;
import lombok.RequiredArgsConstructor;

import static com.ssafy.mugit.user.entity.type.NotificationType.FOLLOW;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;

@RequiredArgsConstructor
public enum NotificationFixture {
    NOTIFICATION_01(USER.getFixture(PROFILE.getFixture()), USER_2.getFixture(PROFILE_2.getFixture()), FOLLOW);

    private final User notifier;
    private final User notified;
    private final NotificationType type;

    public Notification getFixture() {
        return new Notification(notifier, notified, type);
    }
}
