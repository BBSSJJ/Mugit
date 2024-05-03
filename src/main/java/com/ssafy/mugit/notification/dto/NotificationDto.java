package com.ssafy.mugit.notification.dto;

import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.type.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long notifiedId;
    private Long notifierId;
    private NotificationType type;
    private String message;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.notifiedId = notification.getNotified().getId();
        this.notifierId = notification.getNotifier().getId();
        this.type = notification.getType();
        this.message = notification.getMessage();
    }
}
