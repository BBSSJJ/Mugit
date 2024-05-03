package com.ssafy.mugit.user.repository;


import com.ssafy.mugit.user.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
