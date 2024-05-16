package com.ssafy.mugit.notification.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.global.dto.SseMessageDto;
import com.ssafy.mugit.global.entity.SseEvent;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.global.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.entity.NotificationType;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.notification.entity.NotificationType.*;

@Component
@RequiredArgsConstructor
public class NotificationService {

    @Qualifier("RedisMessageBus")
    private final MessageBus messageBus;
    private final NotificationRepository notificationRepository;

    public void sendFollow(User followingUser, User followeeUser) {
        // ※ follow 받는사람이 notified 임을 주의
        Notification notification = new Notification(followeeUser, followingUser, followingUser.getId(), followeeUser.getClass(), FOLLOW);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        messageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FOLLOW, notificationDto));
    }

    public void sendFlowRelease(User flowRecordMaker, User flowAncestor, Flow flow) {
        Notification notification = new Notification(flowAncestor, flowRecordMaker, flow.getId(), flow.getClass(), FLOW_RELEASE);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        messageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FLOW_RELEASE, notificationDto));
    }

    public void sendLikes(User giveLikeUser, User takeLikeUser, Flow flow) {
        Notification notification = new Notification(takeLikeUser, giveLikeUser, flow.getId(), flow.getClass(), LIKE);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        messageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.LIKE, notificationDto));
    }

    public void sendReview(User reviewer, Flow reviewedFlow) {
        Notification notification = new Notification(reviewedFlow.getUser(), reviewer, reviewedFlow.getId(), reviewedFlow.getClass(), NotificationType.REVIEW);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        messageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.REVIEW, notificationDto));
    }

    public List<NotificationDto> findAllNotifications(Long userId) {
        List<NotificationDto> allNotifications = notificationRepository.findAllReadableDtoByUserId(userId);
        if (allNotifications.isEmpty()) throw new UserApiException(UserApiError.NOT_EXIST_READABLE_NOTIFICATION);
        return allNotifications;
    }

    @Transactional
    public void read(Long notificationId, Long userId) {
        Notification notificationInDB = notificationRepository.findByIdWithUserId(notificationId, userId);
        if (notificationInDB == null) throw new UserApiException(UserApiError.NOTIFICATION_NOT_FOUNT);
        notificationInDB.read();
    }

    @Transactional
    public void readAll(Long userId) {
        List<Notification> notificationsInDB = notificationRepository.findAllReadableByUserId(userId);
        notificationsInDB.forEach(Notification::read);
    }
}
