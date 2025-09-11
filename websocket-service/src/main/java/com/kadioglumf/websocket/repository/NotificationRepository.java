package com.kadioglumf.websocket.repository;

import com.kadioglumf.websocket.model.NotificationModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
  List<NotificationModel> findByUserId(Long userId);

  List<NotificationModel> findByUserIdOrderByCreationDateDesc(Long userId);

  List<NotificationModel> findByNotificationIdIn(List<String> notificationIds);
}
