package com.kadioglumf.websocket.payload.request.channel;

import com.kadioglumf.websocket.payload.BaseDto;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;

@Data
public class UpdateChannelRequest implements BaseDto {
  @NotBlank private String name;
  private Set<String> roles;
}
