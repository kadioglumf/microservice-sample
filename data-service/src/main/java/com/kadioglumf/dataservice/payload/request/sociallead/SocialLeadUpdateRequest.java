package com.kadioglumf.dataservice.payload.request.sociallead;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialLeadUpdateRequest {
  @NotNull private Long id;
  private String name;
  private String email;
  private String phone;
}
