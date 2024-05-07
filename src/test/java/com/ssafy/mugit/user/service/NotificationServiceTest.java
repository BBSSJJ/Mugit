package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.notification.service.NotificationService;
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

import java.util.List;

import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_EXIST_READABLE_NOTIFICATION;
import static com.ssafy.mugit.notification.entity.NotificationType.FOLLOW;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE;
import static com.ssafy.mugit.user.fixture.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.user.fixture.UserFixture.USER;
import static com.ssafy.mugit.user.fixture.UserFixture.USER_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("notification")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NotificationServiceTest {

    @Mock
    MessageBus messageBus;

    @Autowired
    NotificationRepository notificationRepository;

    NotificationService sut;

    FollowService followService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    User me;
    User following;

    @BeforeEach
    void setUp() {
        sut = new NotificationService(messageBus, notificationRepository);
        followService = new FollowService(userRepository, followRepository, sut);

        me = USER.getFixture(PROFILE.getFixture());
        following = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.save(me);
        userRepository.save(following);
    }

    @Test
    @DisplayName("[통합] 읽지 않은 알림 없을 때 오류")
    void testFindNoNotification() {
        // given

        // when : NOTHING

        // then
        assertThatThrownBy(() -> sut.findAllNotifications(me.getId()))
                .isInstanceOf(UserApiException.class)
                .hasMessage(NOT_EXIST_READABLE_NOTIFICATION.getMessage());
    }
    
    @Test
    @DisplayName("[통합] 읽지 않은 알림 정상 조회")
    void testFindAllNotifications() {
        // given

        // when
        followService.follow(following.getId(), me.getId());
        List<NotificationDto> allNotifications = sut.findAllNotifications(me.getId());

        // then
        assertThat(following.getProfile().getNickName()).isEqualTo("leaf2");
        assertThat(allNotifications).hasSize(1);
        assertThat(allNotifications.get(0).getDescription()).isEqualTo("leaf2님이 당신을 팔로우합니다.");
    }
    
    @Test
    @DisplayName("[통합] 해당 알림이 없으면 오류(NOTIFICATION_NOT_FOUND)")
    void testFindNotificationNotFound() {
        // given
        long notificationId = -1;

        // when : NOTHING

        // then
        assertThatThrownBy(() -> sut.read(notificationId, me.getId()))
                .isInstanceOf(UserApiException.class)
                .hasMessage(UserApiError.NOTIFICATION_NOT_FOUNT.getMessage());
    }
    
    @Test
    @DisplayName("[통합] 해당 알림 읽기로 변경")
    void testReadNotification() {
        // given
        Notification notification = new Notification(me, following, following.getId(), following.getClass(), FOLLOW);
        notificationRepository.save(notification);
        Notification notificationInDB = notificationRepository.getReferenceById(notification.getId());

        // when
        assertThat(notificationInDB.getIsRead()).isFalse();
        sut.read(notificationInDB.getId(), me.getId());

        // then
        assertThat(notificationInDB.getIsRead()).isTrue();
    }
}