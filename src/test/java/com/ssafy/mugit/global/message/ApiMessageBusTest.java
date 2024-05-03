package com.ssafy.mugit.global.message;

import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.FollowService;
import com.ssafy.mugit.user.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static org.mockito.Mockito.*;

@Tag("notification")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApiMessageBusTest {

    ApiMessageBus sut;

    FollowService followService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Mock
    MessageBus messageBus;

    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        sut = mock(ApiMessageBus.class);
        notificationService = new NotificationService(messageBus);
        followService = new FollowService(userRepository, followRepository, notificationService);

        userRepository.save(USER.getFixture(PROFILE.getFixture()));
        userRepository.save(USER_2.getFixture(PROFILE_2.getFixture()));
    }

    @Test
    @DisplayName("[통합] 팔로우 시 알림 전송")
    void testFollowNotification() {
        // given
        User me = USER.getFixture(PROFILE.getFixture());
        User follower = USER_2.getFixture(PROFILE_2.getFixture());

        // when
        followService.follow(me.getId(), follower.getId());

        // then
        verify(messageBus, times(1)).send(any(NotificationDto.class));
    }
}