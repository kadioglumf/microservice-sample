package com.kadioglumf.dataservice.payload.request.sociallead;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialLeadAssignPartnerRequest {
  @Schema(name = "partnerId", type = "Long", example = "4")
  private Long partnerId;

  @Schema(name = "consultingRequestId", type = "Long", example = "1")
  @NotNull
  private Long socialLeadId;
}
