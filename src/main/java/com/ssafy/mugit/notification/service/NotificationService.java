package com.ssafy.mugit.notification.service;

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
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssafy.mugit.notification.entity.NotificationType.FOLLOW;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final MessageBus ApiMessageBus;
    private final NotificationRepository notificationRepository;

    @SneakyThrows
    @Transactional
    public void sendFollow(User followingUser, User followeeUser) {

        // ※ follow 받는사람이 notified 임을 주의
        Notification notification = new Notification(followeeUser, followingUser, followingUser.getId(), followeeUser.getClass(), FOLLOW);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        ApiMessageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FOLLOW, notificationDto));
    }

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
