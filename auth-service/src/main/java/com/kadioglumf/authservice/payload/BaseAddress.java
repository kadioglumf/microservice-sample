package com.kadioglumf.authservice.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseAddress {
  @NotBlank private String localName;

  @NotBlank private String cantonCode;

  @NotBlank private String bfsNo;

  @NotBlank private String postCode;

  @NotBlank private String region;
  private String number;

  @NotBlank private String street;

  @NotBlank private String cityName;

  @NotBlank private String district;
}
