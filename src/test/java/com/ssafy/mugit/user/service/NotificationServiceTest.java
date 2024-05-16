package com.ssafy.mugit.user.service;

import com.ssafy.mugit.flow.likes.repository.LikesRepository;
import com.ssafy.mugit.flow.likes.service.LikesService;
import com.ssafy.mugit.flow.main.dto.FilePathDto;
import com.ssafy.mugit.flow.main.dto.request.RequestCreateNoteDto;
import com.ssafy.mugit.flow.main.dto.request.RequestReleaseFlowDto;
import com.ssafy.mugit.flow.main.entity.Authority;
import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.flow.main.repository.FlowHashtagRepository;
import com.ssafy.mugit.flow.main.repository.FlowRepository;
import com.ssafy.mugit.flow.main.service.FlowHashtagService;
import com.ssafy.mugit.flow.main.service.FlowService;
import com.ssafy.mugit.flow.review.dto.RequestCreateReviewDto;
import com.ssafy.mugit.flow.review.repository.ReviewRepository;
import com.ssafy.mugit.flow.review.service.ReviewService;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.hashtag.repository.HashtagRepository;
import com.ssafy.mugit.hashtag.service.HashtagService;
import com.ssafy.mugit.global.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.notification.service.NotificationService;
import com.ssafy.mugit.record.repository.RecordRepository;
import com.ssafy.mugit.record.repository.RecordSourceRepository;
import com.ssafy.mugit.record.repository.SourceRepository;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE;
import static com.ssafy.mugit.fixure.ProfileFixture.PROFILE_2;
import static com.ssafy.mugit.fixure.UserFixture.USER;
import static com.ssafy.mugit.fixure.UserFixture.USER_2;
import static com.ssafy.mugit.global.exception.error.UserApiError.NOT_EXIST_READABLE_NOTIFICATION;
import static com.ssafy.mugit.notification.entity.NotificationType.FOLLOW;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    FlowRepository flowRepository;

    @Autowired
    RecordRepository recordRepository;

    @Autowired
    RecordSourceRepository recordSourceRepository;

    @Autowired
    SourceRepository sourceRepository;

    @Autowired
    HashtagRepository hashtagRepository;

    @Autowired
    FlowHashtagRepository flowHashtagRepository;

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    TestEntityManager testEntityManager;

    FollowService followService;
    FlowService flowService;
    HashtagService hashtagService;
    FlowHashtagService flowHashtagService;
    LikesService likesService;
    ReviewService reviewService;
    NotificationService sut;

    User me;
    User following;

    @BeforeEach
    void setUp() {
        sut = new NotificationService(messageBus, notificationRepository);
        followService = new FollowService(userRepository, followRepository, sut);
        hashtagService = new HashtagService(hashtagRepository);
        flowHashtagService = new FlowHashtagService(flowHashtagRepository);
        flowService = new FlowService(flowRepository, recordRepository, recordSourceRepository, sourceRepository, userRepository, hashtagService, flowHashtagService, sut);
        likesService = new LikesService(likesRepository, userRepository, flowRepository, sut);
        reviewService = new ReviewService(reviewRepository, userRepository, flowRepository, sut);

        me = USER.getFixture(PROFILE.getFixture());
        following = USER_2.getFixture(PROFILE_2.getFixture());
        userRepository.saveAll(List.of(me, following));
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
    void testFindAllNotifications() throws InterruptedException {
        // given

        // when 1 : follow
        followService.follow(following.getId(), me.getId());

        // when 2 : release flow
        flowService.create(me.getId(), new RequestCreateNoteDto("new flow", "create flow message", Authority.PUBLIC,
                List.of(new FilePathDto("source", "source file 1", "https://mugit.site/files/source_file_1")), null));
        Thread.sleep(1000L);
        Flow noteFlow = flowRepository.findAllByUserId(me.getId()).get(0);
        flowService.regist(following.getId(), noteFlow.getId());
        Thread.sleep(1000L);

        Flow followFlow = flowRepository.findUnreleasedFlowsByUserId(following.getId()).get(0);
        flowService.release(following.getId(), followFlow.getId(), new RequestReleaseFlowDto("follow new flow", "release flow message", Authority.PROTECTED,
                List.of(new FilePathDto("source", "source file 2", "https://mugit.site/files/source_file_2")), null));
        Thread.sleep(1000L);

        // when 3 : like
        likesService.changeLikes(following.getId(), noteFlow.getId());
        Thread.sleep(1000L);

        // when 4 : review
        reviewService.createReview(following.getId(), noteFlow.getId(), new RequestCreateReviewDto("여기 좋네요", "00:00"));

        // then
        List<NotificationDto> allNotifications = sut.findAllNotifications(me.getId());
        assertThat(allNotifications).hasSize(4);
        // 알림 정렬 테스트 병행
        assertThat(allNotifications.get(3)).satisfies(notificationDto ->  assertThat(notificationDto.getDescription()).isEqualTo("leaf2님이 당신을 팔로우합니다."));
        assertThat(allNotifications.get(2)).satisfies(notificationDto ->  assertThat(notificationDto.getDescription()).isEqualTo("leaf2님이 [" + noteFlow.getTitle() + "] 플로우에서 릴리즈합니다."));
        assertThat(allNotifications.get(1)).satisfies(notificationDto ->  assertThat(notificationDto.getDescription()).isEqualTo("leaf2님이 [" + noteFlow.getTitle() + "] 플로우를 좋아합니다."));
        assertThat(allNotifications.get(0)).satisfies(notificationDto ->  assertThat(notificationDto.getDescription()).isEqualTo("leaf2님이 [" + noteFlow.getTitle() + "] 플로우에 리뷰를 남겼습니다."));
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
        Notification notification = new Notification(me, following, following, FOLLOW);
        notificationRepository.save(notification);
        Notification notificationInDB = notificationRepository.getReferenceById(notification.getId());

        // when
        assertThat(notificationInDB.getIsRead()).isFalse();
        sut.read(notificationInDB.getId(), me.getId());

        // then
        assertThat(notificationInDB.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("[통합] 알림 모두읽기")
    void testReadAllNotifications() {
        // given
        Notification notification = new Notification(me, following, following, FOLLOW);
        Notification notification2 = new Notification(me, following, following, FOLLOW);
        Notification notification3 = new Notification(me, following, following, FOLLOW);
        notificationRepository.saveAll(List.of(notification, notification2, notification3));
        List<Notification> notificationsInDB = notificationRepository.findAll();

        // when
        assertThat(notificationsInDB).allSatisfy(notificationInDB -> assertThat(notificationInDB.getIsRead()).isFalse());
        sut.readAll(me.getId());

        // then
        assertThat(notificationsInDB).allSatisfy(notificationInDB -> assertThat(notificationInDB.getIsRead()).isTrue());
    }
}