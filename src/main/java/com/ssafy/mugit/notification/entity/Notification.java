package com.ssafy.mugit.notification.entity;

import com.ssafy.mugit.flow.main.entity.Flow;
import com.ssafy.mugit.global.entity.BaseTimeEntity;
import com.ssafy.mugit.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.ssafy.mugit.notification.entity.NotificationType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

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
    private String causeEntityClass;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "description", nullable = false)
    private String description;


    public Notification(User notified, User notifier, Object causeEntity, NotificationType type) {
        this.notified = notified;
        this.notifier = notifier;
        this.isRead = false;
        switch (type){
            case FOLLOW -> {
                this.type = FOLLOW;
                User causeEntityUser = (User) causeEntity;
                this.causeEntityId = causeEntityUser.getId();
                this.causeEntityClass = causeEntityUser.getClass().getSimpleName();
                this.description = notifier.getProfile().getNickName() + "님이 당신을 팔로우합니다.";
            }
            case LIKE -> {
                this.type = LIKE;
                Flow causeEntityFlow = (Flow) causeEntity;
                this.causeEntityId = causeEntityFlow.getId();
                this.causeEntityClass = causeEntityFlow.getClass().getSimpleName();
                this.description = notifier.getProfile().getNickName() + "님이 [" + causeEntityFlow.getTitle() + "] 플로우를 좋아합니다.";
            }
            case FLOW_RELEASE -> {
                this.type = FLOW_RELEASE;
                Flow causeEntityFlow = (Flow) causeEntity;
                this.causeEntityId = causeEntityFlow.getId();
                this.causeEntityClass = causeEntityFlow.getClass().getSimpleName();
                this.description = notifier.getProfile().getNickName() + "님이 [" + causeEntityFlow.getTitle() + "] 플로우에서 릴리즈합니다.";
            }
            case REVIEW -> {
                this.type = REVIEW;
                Flow causeEntityFlow = (Flow) causeEntity;
                this.causeEntityId = causeEntityFlow.getId();
                this.causeEntityClass = causeEntityFlow.getClass().getSimpleName();
                this.description = notifier.getProfile().getNickName() + "님이 [" + causeEntityFlow.getTitle() + "] 플로우에 리뷰를 남겼습니다.";
            }
        }
    }

    public void read() {
        this.isRead = true;
    }
}
