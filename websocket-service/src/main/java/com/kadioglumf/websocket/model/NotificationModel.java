package com.kadioglumf.websocket.model;

import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.payload.WsCategoryTypeConverter;
import com.kadioglumf.websocket.payload.WsInfoTypeConverter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "notification",
    indexes = {@Index(name = "idx_user_id_is_deleted", columnList = "userId, isDeleted")})
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SQLRestriction(value = "is_deleted = false")
public class NotificationModel extends AbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_id_generator")
  @SequenceGenerator(
      name = "notification_id_generator",
      sequenceName = "seq_notification_id_generator",
      allocationSize = 1)
  private Long id;

  private String notificationId;
  private String message;

  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private String channel;
  private Long userId;
  private boolean isRead;
  private boolean isDeleted;
}
