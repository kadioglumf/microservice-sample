package com.kadioglumf.websocket.payload.request;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.enums.WsSendingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WsSendMessageRequest {
  private String channel;
  private Object payload;
  private WsCategoryType category;
  private WsInfoType infoType;
  private WsSendingType sendingType;
  private RoleTypeEnum role;
  private Long userId;
}
