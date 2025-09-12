package com.kadioglumf.authservice.payload.response;

import lombok.Data;

import java.util.Set;

@Data
public class GetUserDetailsResponse {
  private Long userId;
  private String name;
  private String surname;
  private String email;
  private String phoneNumber;
  private Set<String> roles;
}
