package com.kadioglumf.dataservice.payload.response.contactus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateContactUsCommentRequest {

  @NotBlank
  @Schema(name = "contactRequestId", type = "String", example = "CT23423")
  private String contactRequestId;

  @NotBlank
  @Size(max = 255)
  @Schema(name = "message", type = "String", example = "Kullanici ile gorusuldu.")
  private String message;
}
