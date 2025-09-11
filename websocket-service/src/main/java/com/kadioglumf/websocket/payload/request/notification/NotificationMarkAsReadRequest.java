package com.kadioglumf.websocket.payload.request.notification;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class NotificationMarkAsReadRequest {
  @NotNull
  @Size(min = 1)
  private List<String> notificationIds;
}
