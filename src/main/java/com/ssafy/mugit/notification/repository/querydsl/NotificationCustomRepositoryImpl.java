package com.ssafy.mugit.notification.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.mugit.global.dto.NotificationDto;
import com.ssafy.mugit.global.dto.QNotificationDto;
import com.ssafy.mugit.notification.entity.Notification;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.ssafy.mugit.notification.entity.QNotification.notification;
import static com.ssafy.mugit.user.entity.QUser.user;

public class NotificationCustomRepositoryImpl implements NotificationCustomRepository {
    private final JPAQueryFactory queryFactory;

    public NotificationCustomRepositoryImpl(@Autowired EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Notification> findAllReadableByUserId(Long userId) {
        return queryFactory.selectFrom(notification)
                .leftJoin(notification.notified, user)
                .where(notification.isRead.eq(false).and(user.id.eq(userId)))
                .stream().toList();
    }

    @Override
    public List<NotificationDto> findAllReadableDtoByUserId(Long userId) {
        return queryFactory.select(new QNotificationDto(notification))
                .from(notification)
                .leftJoin(notification.notified, user)
                .where(notification.isRead.eq(false).and(user.id.eq(userId)))
                .stream().toList();
    }

    @Override
    public Notification findByIdWithUserId(Long notificationId, Long userId) {
        return queryFactory.select(notification)
                .from(notification)
                .leftJoin(user).on(notification.notified.eq(user))
                .where(notification.id.eq(notificationId).and(user.id.eq(userId)))
                .fetchOne();
    }
}
