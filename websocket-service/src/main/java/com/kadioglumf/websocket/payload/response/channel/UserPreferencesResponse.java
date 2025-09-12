package com.kadioglumf.websocket.payload.response.channel;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferencesResponse implements Serializable {
  private Long userId;
  private boolean isSubscribed;
  private String channel;
}
