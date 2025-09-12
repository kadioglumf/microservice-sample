package com.kadioglumf.websocket.event;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.kafka.DefaultDomainEvent;
import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.enums.WsSendingType;
import com.kadioglumf.websocket.payload.WsInfoTypeConverter;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseWebsocketEvent extends DefaultDomainEvent {
  private Object message;

  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @Convert(converter = WsCategoryType.class)
  private WsCategoryType category;

  private String channel;
  private WsSendingType sendingType;
  private RoleTypeEnum role;
}
