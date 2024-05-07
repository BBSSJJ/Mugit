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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User notified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User notifier;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "message", nullable = false)
    private String message;

    public Notification(User notifier, User notified, NotificationType type) {
        switch (type){
            case FOLLOW -> {
                this.type = FOLLOW;
                this.notifier = notifier;
                this.notified = notified;
                this.isRead = false;
                this.message = notifier.getProfile().getNickName() + "님이 당신을 팔로우합니다.";
            }
            case LIKE -> {
                this.type = LIKE;
            }
            case FLOW_RELEASE -> {
                this.type = FLOW_RELEASE;
            }
        }
    }
}
