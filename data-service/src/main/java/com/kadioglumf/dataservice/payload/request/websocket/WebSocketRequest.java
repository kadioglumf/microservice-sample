package com.kadioglumf.dataservice.payload.request.websocket;

import com.kadioglumf.dataservice.core.dto.BaseDto;
import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
import com.kadioglumf.dataservice.enums.WsCategoryType;
import com.kadioglumf.dataservice.enums.WsInfoType;
import com.kadioglumf.dataservice.enums.WsSendingType;
import lombok.Data;

@Data
public class WebSocketRequest implements BaseDto {
  private Object message;
  private WsInfoType infoType;
  private WsCategoryType category;
  private String channel;
  private WsSendingType sendingType;
  private RoleTypeEnum role;
  private Long userId;
}
