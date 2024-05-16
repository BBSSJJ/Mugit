package com.ssafy.mugit.global.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.entity.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private Long notifiedId;
    private Long notifierId;
    private Long causeEntityId;
    private NotificationType type;
    private String description;
    private String createAt;

    @QueryProjection
    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.notifiedId = notification.getNotified().getId();
        this.notifierId = notification.getNotifier().getId();
        this.causeEntityId = notification.getCauseEntityId();
        this.type = notification.getType();
        this.description = notification.getDescription();
        this.createAt = notification.getCreatedAt().toString();
    }
}
