package com.kadioglumf.authservice.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kadioglumf.authservice.converter.GenderConverter;
import com.kadioglumf.authservice.enums.GenderEnum;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class SignupRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String name;

  @NotBlank
  @Size(min = 3, max = 20)
  private String surname;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

  @NotBlank
  @Size(max = 20)
  private String mobile;

  @NotBlank
  @Size(max = 30)
  private String nationality;

  @NotNull
  @JsonFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthDay;

  @NotNull
  @Convert(converter = GenderConverter.class)
  private GenderEnum gender;

  @NotBlank private String recaptchaResponse;

  private AddressRequest address;
}
