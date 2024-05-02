package com.ssafy.mugit.user.service;

import com.ssafy.mugit.user.entity.Notification;
import com.ssafy.mugit.user.entity.User;
import com.ssafy.mugit.user.entity.type.NotificationType;

import static com.ssafy.mugit.user.entity.type.NotificationType.FOLLOW;

public class NotificationService {
    public void emitFollowNotification(User following, User follower) {

    }

    public Notification createNotification(User following, User follower) {
        return new Notification(following, follower, FOLLOW);
    }
}
