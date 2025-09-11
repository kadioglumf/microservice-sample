package com.kadioglumf.dataservice.payload.request.sociallead;

import com.kadioglumf.dataservice.enums.SocialLeadStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialLeadUpdateStatusRequest {
  @NotNull private Long id;
  @NotNull private SocialLeadStatus status;
}
