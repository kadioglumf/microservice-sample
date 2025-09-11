package com.kadioglumf.authservice.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kadioglumf.authservice.converter.GenderConverter;
import com.kadioglumf.authservice.enums.GenderEnum;
import com.kadioglumf.authservice.payload.AddressResponse;
import jakarta.persistence.Convert;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserDetailsResponse {
  private String name;
  private String surname;
  private AddressResponse address;

  @JsonFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthDay;

  private String mobile;
  private String nationality;

  @Convert(converter = GenderConverter.class)
  private GenderEnum gender;
}
