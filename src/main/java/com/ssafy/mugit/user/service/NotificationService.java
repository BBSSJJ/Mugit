package com.ssafy.mugit.user.service;

import com.ssafy.mugit.global.dto.SseEvent;
import com.ssafy.mugit.global.message.MessageBus;
import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.notification.dto.SseMessageDto;
import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.mugit.user.entity.type.NotificationType.FOLLOW;

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
