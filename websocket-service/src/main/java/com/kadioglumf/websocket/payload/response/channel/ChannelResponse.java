package com.kadioglumf.websocket.payload.response.channel;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelResponse implements Serializable {
  private String name;
  private Set<String> roles;
  private List<UserPreferencesResponse> userPreferences;
}
