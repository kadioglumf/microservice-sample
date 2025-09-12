package com.kadioglumf.websocket.payload.request.channel;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class AddChannelRequest implements Serializable {
  @NotBlank private String name;
  private Set<String> roles;
}
