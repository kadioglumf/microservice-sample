package com.kadioglumf.authservice.enums;

import lombok.Getter;

@Getter
public enum OAuth2ProviderEnum {
  LOCAL("local"),
  GOOGLE("google"),
  FACEBOOK("facebook");

  private final String providerType;

  OAuth2ProviderEnum(String providerType) {
    this.providerType = providerType;
  }
}
