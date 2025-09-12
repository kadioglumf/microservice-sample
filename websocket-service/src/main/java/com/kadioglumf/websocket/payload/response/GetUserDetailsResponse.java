package com.kadioglumf.websocket.payload.response;

import java.io.Serializable;
import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsResponse implements Serializable {
  private Long userId;
  private String name;
  private String surname;
  private String email;
  private String phoneNumber;
  private Set<String> roles;
}
