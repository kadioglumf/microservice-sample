package com.kadioglumf.websocket.payload.request.channel;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

@Data
public class SubscribeChannelRequest implements Serializable {
  @NotBlank private String name;
}
