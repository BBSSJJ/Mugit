package com.ssafy.mugit.notification.fixture;


import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import lombok.RequiredArgsConstructor;

import static com.ssafy.mugit.notification.fixture.NotificationFixture.NOTIFICATION_01;

@RequiredArgsConstructor
public enum NotificationDtoFixture {
    NOTIFICATION_DTO_01(NOTIFICATION_01.getFixture());

    private final Notification notification;

    public NotificationDto getFixture() {
        return new NotificationDto(notification);
    }
}
