package com.kadioglumf.authservice.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kadioglumf.authservice.converter.GenderConverter;
import com.kadioglumf.authservice.enums.GenderEnum;
import jakarta.persistence.Convert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateUserDetailsRequest {

  @NotBlank
  @Size(min = 3, max = 20)
  private String name;

  @NotBlank
  @Size(min = 3, max = 20)
  private String surname;

  @NotNull @Valid private AddressRequest address;

  @NotNull
  @JsonFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthDay;

  @NotBlank
  @Size(max = 20)
  private String mobile;

  @NotBlank
  @Size(max = 30)
  private String nationality;

  @NotNull
  @Convert(converter = GenderConverter.class)
  private GenderEnum gender;
}
