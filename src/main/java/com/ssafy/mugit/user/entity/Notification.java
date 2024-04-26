package com.ssafy.mugit.user.entity;

import com.ssafy.mugit.user.entity.type.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true, nullable = false)
    private User user;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType type;

    @Column(name = "message", nullable = false)
    private String message;
}
