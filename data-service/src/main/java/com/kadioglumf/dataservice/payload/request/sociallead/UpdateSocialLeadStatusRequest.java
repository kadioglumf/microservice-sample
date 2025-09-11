package com.kadioglumf.dataservice.payload.request.sociallead;

import com.kadioglumf.dataservice.enums.SocialLeadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateSocialLeadStatusRequest {

  @NotNull
  @Schema(name = "socialLeadId", type = "Long", example = "1")
  private Long socialLeadId;

  @NotNull
  @Schema(name = "status", type = "String", example = "OPEN")
  private SocialLeadStatus status;
}
