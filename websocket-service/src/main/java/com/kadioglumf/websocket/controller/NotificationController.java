package com.kadioglumf.websocket.controller;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.secure.Secure;
import com.kadioglumf.websocket.payload.request.notification.NotificationDeleteRequest;
import com.kadioglumf.websocket.payload.request.notification.NotificationMarkAsReadRequest;
import com.kadioglumf.websocket.payload.response.notification.NotificationResponse;
import com.kadioglumf.websocket.service.NotificationService;
import com.kadioglumf.websocket.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notification Documentation", description = "Notification Documentation")
@RestController
@RequestMapping("/websocket/notification")
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;
  private final WebSocketService webSocketService;

  @GetMapping(value = "/getAll")
  @Operation(summary = "Gets notifications of user")
  @Secure(role = {RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_USER})
  public ResponseEntity<List<NotificationResponse>> fetch() {
    return ResponseEntity.ok(notificationService.fetch());
  }

  @DeleteMapping(value = "/deleteAll")
  @Operation(summary = "Deletes all notifications of user")
  @Secure(role = {RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_USER})
  public ResponseEntity<Void> deleteAll() {
    notificationService.deleteAll();
    return ResponseEntity.ok().build();
  }

  @PutMapping(value = "/markAsRead")
  @Operation(summary = "Marks notifications of user as read")
  @Secure(role = {RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_USER})
  public ResponseEntity<Void> markAsRead(
      @RequestBody @Valid NotificationMarkAsReadRequest request) {
    notificationService.markAsRead(request.getNotificationIds());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(value = "/deleteByIds")
  @Operation(summary = "Delete notifications of user")
  @Secure(role = {RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_USER})
  public ResponseEntity<Void> deleteByNotificationIds(
      @RequestBody @Valid NotificationDeleteRequest request) {
    notificationService.deleteByNotificationIds(request.getNotificationIds());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/cache/refresh")
  @Operation(summary = "Refresh Notifications")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Void> refresh() {
    notificationService.refreshNotifications();
    return ResponseEntity.ok().build();
  }

  /*  @PostMapping("/sendNotification")
  @Secure(role = RoleTypeEnum.ROLE_SYSTEM)
  public void sendNotification(@RequestBody @Valid WebSocketRequest request) {
    webSocketService.sendNotification(request);
  }*/
}
