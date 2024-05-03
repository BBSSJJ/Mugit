package com.ssafy.mugit.notification.fixture;


import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.type.NotificationType;
import com.ssafy.mugit.user.fixture.UserFixture;
import lombok.RequiredArgsConstructor;

import static com.ssafy.mugit.notification.fixture.NotificationFixture.NOTIFICATION_01;
import static com.ssafy.mugit.user.entity.type.NotificationType.FOLLOW;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;

@RequiredArgsConstructor
public enum NotificationDtoFixture {
    NOTIFICATION_DTO_01(NOTIFICATION_01.getFixture());

    private final Notification notification;

    public NotificationDto getFixture() {
        return new NotificationDto(notification);
    }
}
