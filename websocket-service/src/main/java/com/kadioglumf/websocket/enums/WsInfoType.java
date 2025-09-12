package com.kadioglumf.websocket.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum WsInfoType {
  @JsonProperty("new_user")
  NEW_USER("new_user"),
  ;

  private final String value;

  WsInfoType(String value) {
    this.value = value;
  }

  public String toString() {
    return this.value;
  }

  public static WsInfoType toAttribute(String value) {
    return Arrays.stream(WsInfoType.values())
        .filter(op -> op.toString().equals(value))
        .findFirst()
        .orElseThrow();
  }
}
