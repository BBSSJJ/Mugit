package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Tag("notification")
class NotificationServiceTest {

    NotificationService sut;

    @BeforeEach
    void setUp() {
        sut = new NotificationService();
    }

}