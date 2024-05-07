package com.ssafy.mugit.notification.repository;


import com.ssafy.mugit.notification.entity.Notification;
import com.ssafy.mugit.notification.repository.querydsl.NotificationCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {
}
