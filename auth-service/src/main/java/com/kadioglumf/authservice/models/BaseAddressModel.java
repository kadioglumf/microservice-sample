package com.kadioglumf.authservice.models;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseAddressModel extends AbstractModel {
  @Size(max = 50)
  private String localName;

  @Size(max = 2)
  private String cantonCode;

  @Size(max = 5)
  private String bfsNo;

  @Size(max = 5)
  private String postCode;

  @Size(max = 10)
  private String region;

  @Size(max = 5)
  private String number;

  @Size(max = 75)
  private String street;

  @Size(max = 50)
  private String cityName;

  @Size(max = 50)
  private String district;
}
