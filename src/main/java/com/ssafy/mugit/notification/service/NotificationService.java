package com.ssafy.mugit.notification.service;

import com.ssafy.mugit.global.entity.SseEvent;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.global.dto.SseMessageDto;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.notification.entity.NotificationType.FOLLOW;

@Component
@RequiredArgsConstructor
public class NotificationService {

    private final MessageBus ApiMessageBus;
    private final NotificationRepository notificationRepository;

    @SneakyThrows
    @Transactional
    public void sendFollow(User follower, User following) {
        Notification notification = new Notification(follower, following, FOLLOW);
        notificationRepository.save(notification);
        NotificationDto notificationDto = new NotificationDto(notification);
        ApiMessageBus.send(new SseMessageDto<NotificationDto>(notificationDto.getNotifiedId(), SseEvent.FOLLOW, notificationDto));
    }
}
