package com.ssafy.mugit.notification.entity;

import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ssafy.mugit.notification.entity.NotificationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notified_user_id", nullable = false)
    private User notified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notifier_user_id", nullable = false)
    private User notifier;

    @Column(name = "cause_entity_id", nullable = false)
    private Long causeEntityId;

    @Column(name = "cause_entity_class", nullable = false)
    private Class<?> causeEntityClass;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "description", nullable = false)
    private String description;


    public Notification(User notified, User notifier, Long causeEntityId, Class<?> causeEntityClass, NotificationType type) {
        switch (type){
            case FOLLOW -> {
                this.type = FOLLOW;
                this.notified = notified;
                this.notifier = notifier;
                this.isRead = false;
                this.causeEntityId = causeEntityId;
                this.causeEntityClass = causeEntityClass;
                this.description = notifier.getProfile().getNickName() + "님이 당신을 팔로우합니다.";
            }
            case LIKE -> {
                this.type = LIKE;
            }
            case FLOW_RELEASE -> {
                this.type = FLOW_RELEASE;
            }
        }
    }

    public void read() {
        this.isRead = true;
    }
}
