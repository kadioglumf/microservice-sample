package com.kadioglumf.websocket.payload.response.channel;

import com.kadioglumf.websocket.payload.BaseDto;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChannelResponse implements BaseDto {
  private String name;
  private Set<String> roles;
  private List<UserPreferencesResponse> userPreferences;
}
