package com.kadioglumf.dataservice.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorUserResponse {
  private String name;
  private String surname;
}
