package com.kadioglumf.websocket.payload.request.channel;

import com.kadioglumf.websocket.payload.BaseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscribeChannelRequest implements BaseDto {
  @NotBlank private String name;
}
