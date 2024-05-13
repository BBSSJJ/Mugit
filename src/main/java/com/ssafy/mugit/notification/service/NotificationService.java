package com.ssafy.mugit.notification.service;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.global.dto.SseMessageDto;
import com.ssafy.mugit.global.entity.SseEvent;
import com.ssafy.mugit.global.exception.UserApiException;
import com.ssafy.mugit.global.exception.error.UserApiError;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import com.ssafy.mugit.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ssafy.mugit.notification.entity.NotificationType.*;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final MessageBus ApiMessageBus;
    private final NotificationRepository notificationRepository;

    public void sendFollow(User followingUser, User followeeUser) {
        // ※ follow 받는사람이 notified 임을 주의
        Notification notification = new Notification(followeeUser, followingUser, followingUser.getId(), followeeUser.getClass(), FOLLOW);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        ApiMessageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FOLLOW, notificationDto));
    }

    public void sendFlowRelease(User flowRecordMaker, User flowAncestor, Flow flow) {
        Notification notification = new Notification(flowAncestor, flowRecordMaker, flow.getId(), flow.getClass(), FLOW_RELEASE);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        ApiMessageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FLOW_RELEASE, notificationDto));
    }

    public void sendLikes(User giveLikeUser, User takeLikeUser, Flow flow) {
        Notification notification = new Notification(giveLikeUser, takeLikeUser, flow.getId(), flow.getClass(), LIKE);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        ApiMessageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FLOW_RELEASE, notificationDto));
    }

    public void sendReview(User reviewer, User reviewReceiver, Flow reviewedFlow) {}

    public List<NotificationDto> findAllNotifications(Long userId) {
        List<NotificationDto> allNotifications = notificationRepository.findAllReadableByUserId(userId);
        if (allNotifications.isEmpty()) throw new UserApiException(UserApiError.NOT_EXIST_READABLE_NOTIFICATION);
        return allNotifications;
    }

    public void read(Long notificationId, Long userId) {
        Notification notificationInDB = notificationRepository.findByIdWithUserId(notificationId, userId);
        if (notificationInDB == null) throw new UserApiException(UserApiError.NOTIFICATION_NOT_FOUNT);
        notificationInDB.read();
    }
}
