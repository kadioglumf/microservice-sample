package com.kadioglumf.websocket.payload.response.channel;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChannelResponse implements Serializable {
  private String name;
  private boolean isSubscribed;
}
