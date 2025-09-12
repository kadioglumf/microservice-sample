package com.kadioglumf.websocket.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum WsCategoryType {
  @JsonProperty("new_user_registered")
  NEW_USER_REGISTERED("new_user_registered"),
  ;

  private final String value;

  WsCategoryType(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }

  public static WsCategoryType toAttribute(String value) {
    return Arrays.stream(WsCategoryType.values())
        .filter(op -> op.toString().equals(value))
        .findFirst()
        .orElseThrow();
  }
}
