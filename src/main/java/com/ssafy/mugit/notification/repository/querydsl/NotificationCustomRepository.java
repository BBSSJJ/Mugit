package com.ssafy.mugit.notification.repository.querydsl;

import com.ssafy.mugit.notification.dto.NotificationDto;
import com.ssafy.mugit.notification.entity.Notification;

import java.util.List;

public interface NotificationCustomRepository {
    List<NotificationDto> findAllReadableByUserId(Long userId);

    Notification findByIdWithUserId(Long notificationId, Long userId);
}
