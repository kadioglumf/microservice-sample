package com.kadioglumf.websocket.payload.request.adapter;

import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsByRolesRequest {
  private Set<String> roles;
}
