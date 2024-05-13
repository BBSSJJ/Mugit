package com.ssafy.mugit.global.message;

import com.ssafy.mugit.global.dto.SseMessageDto;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.FollowRepository;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.user.repository.UserRepository;
import com.ssafy.mugit.user.service.FollowService;
import com.ssafy.mugit.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static com.ssafy.mugit.fixure.UserFixture.USER_2;
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

    @Autowired
    NotificationRepository notificationRepository;

    @Mock
    MessageBus messageBus;

    NotificationService notificationService;

    User follower;
    User followee;

    @BeforeEach
    void setUp() {
        sut = mock(ApiMessageBus.class);
        notificationService = new NotificationService(messageBus, notificationRepository);
        followService = new FollowService(userRepository, followRepository, notificationService);

        follower = USER.getFixture(PROFILE.getFixture());
        userRepository.save(follower);
        followee = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(followee);
    }

    @Test
    @DisplayName("[통합] 팔로우 시 알림 전송")
    void testFollowNotification() {
        // given
        // when
        followService.follow(follower.getId(), followee.getId());

        // then
        verify(messageBus, times(1)).send(any(SseMessageDto.class));
    }
}