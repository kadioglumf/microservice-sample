package com.kadioglumf.websocket.payload.response.channel;

import com.kadioglumf.websocket.payload.BaseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChannelResponse implements BaseDto {
  private String name;
  private boolean isSubscribed;
}
