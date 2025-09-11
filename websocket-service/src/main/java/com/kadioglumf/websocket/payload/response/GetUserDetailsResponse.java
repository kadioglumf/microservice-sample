package com.kadioglumf.websocket.payload.response;

import com.kadioglumf.websocket.payload.BaseDto;
import lombok.Data;

@Data
public class GetUserDetailsResponse implements BaseDto {
  private Long userId;
  private String name;
  private String surname;
  private String email;
  private String phoneNumber;
  private String role;
}
