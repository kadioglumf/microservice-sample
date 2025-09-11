package com.kadioglumf.dataservice.payload.request.contactus;

import com.kadioglumf.dataservice.converter.GenderConverter;
import com.kadioglumf.dataservice.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateContactUsRequest {
  @NotBlank
  @Schema(name = "name", type = "String", example = "Max")
  private String name;

  @NotBlank
  @Schema(name = "surname", type = "String", example = "Carl")
  private String surname;

  @NotBlank private String email;

  @NotBlank
  @Schema(name = "phone", type = "String", example = "+41761112233")
  private String phone;

  @NotBlank
  @Schema(name = "subject", type = "String", example = "Test Request Subject")
  private String subject;

  @NotBlank
  @Schema(
      name = "message",
      type = "String",
      example = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
  private String message;

  @Schema(name = "gender", type = "Gender", example = "male")
  @Convert(converter = GenderConverter.class)
  private Gender gender;

  @Schema(name = "recaptchaResponse", type = "String", example = "qazwsxedcrfv")
  @NotBlank
  private String recaptchaResponse;
}
