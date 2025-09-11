package com.kadioglumf.dataservice.model;

import jakarta.persistence.MappedSuperclass;
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
  private String localName;
  private String cantonCode;
  private String bfsNo;
  private String postCode;
  private String region;
  private String number;
  private String street;
  private String cityName;
  private String district;
}
