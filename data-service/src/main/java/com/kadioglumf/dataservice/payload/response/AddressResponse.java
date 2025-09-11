package com.kadioglumf.dataservice.payload.response;

import lombok.Data;

@Data
public class AddressResponse {
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
