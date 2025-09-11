package com.kadioglumf.dataservice.payload.request.sociallead;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateSocialLeadCommentRequest {

  @NotNull
  @Schema(name = "id", type = "Long", example = "1")
  private Long id;

  @NotBlank
  @Size(max = 255)
  @Schema(name = "message", type = "String", example = "Kullanici ile gorusuldu.")
  private String message;
}
