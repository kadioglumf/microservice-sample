package com.kadioglumf.authservice.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccessTokenResponse {
  private String accessToken;
  private String refreshToken;
}
