package com.kadioglumf.dataservice.payload.request.contactus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignContactUsRequest {

  @NotNull
  @Schema(name = "userId", type = "Long", example = "2")
  private Long userId;

  @NotNull
  @Schema(name = "contactRequestId", type = "String", example = "CT12345")
  private String contactRequestId;
}
