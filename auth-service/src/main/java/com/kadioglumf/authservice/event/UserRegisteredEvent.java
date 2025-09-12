package com.kadioglumf.authservice.event;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.kafka.DefaultDomainEvent;
import com.kadioglumf.authservice.enums.WsCategoryType;
import com.kadioglumf.authservice.enums.WsInfoType;
import com.kadioglumf.authservice.enums.WsSendingType;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserRegisteredEvent extends DefaultDomainEvent {
  private Object message;
  private WsInfoType infoType;
  private WsCategoryType category;
  private String channel;
  private WsSendingType sendingType;
  private RoleTypeEnum role;
}
