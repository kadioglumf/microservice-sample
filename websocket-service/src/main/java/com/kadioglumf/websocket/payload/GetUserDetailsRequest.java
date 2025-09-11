package com.kadioglumf.websocket.payload;

import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsRequest {
  private Set<Long> userIds;
}
