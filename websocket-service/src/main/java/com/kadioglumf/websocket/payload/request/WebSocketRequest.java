package com.kadioglumf.websocket.payload.request;

import com.kadioglumf.websocket.core.dto.BaseDto;
import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.enums.WsSendingType;
import com.kadioglumf.websocket.payload.WsInfoTypeConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketRequest implements BaseDto {
  @NotNull private Object message;

  @NotNull
  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  @NotNull
  @Convert(converter = WsCategoryType.class)
  private WsCategoryType category;

  @NotBlank private String channel;

  private WsSendingType sendingType;
  private RoleTypeEnum role;
  private Long userId;
}
