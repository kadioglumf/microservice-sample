package com.kadioglumf.authservice.payload.response;

import lombok.Data;

@Data
public class GetUserDetailsResponse {
  private Long userId;
  private String name;
  private String surname;
  private String email;
  private String phoneNumber;
  private String role;
}
