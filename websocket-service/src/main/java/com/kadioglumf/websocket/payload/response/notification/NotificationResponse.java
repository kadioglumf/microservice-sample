package com.kadioglumf.websocket.payload.response.notification;

import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.payload.WsCategoryTypeConverter;
import com.kadioglumf.websocket.payload.WsInfoTypeConverter;
import jakarta.persistence.Convert;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class NotificationResponse implements Serializable {
  private String notificationId;
  private String message;

  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private String channel;
  private Long userId;
  private boolean isRead;
  private Date creationDate;
}
