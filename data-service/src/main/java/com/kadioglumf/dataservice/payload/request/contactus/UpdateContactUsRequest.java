package com.kadioglumf.dataservice.payload.request.contactus;

import com.kadioglumf.dataservice.enums.ContactUsStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateContactUsRequest {
  @NotBlank
  @Schema(name = "contactRequestId", type = "String", example = "CT123456")
  private String contactRequestId;

  @NotNull
  @Schema(name = "status", type = "ContactUsStatus", example = "IN_PROCESS")
  private ContactUsStatus status;
}
