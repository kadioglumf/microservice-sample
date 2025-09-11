package com.kadioglumf.websocket.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum WsInfoType {
  @JsonProperty("new_offer")
  NEW_OFFER("new_offer"),
  @JsonProperty("updated_offer")
  UPDATED_OFFER("updated_offer"),
  @JsonProperty("deleted_offer")
  DELETED_OFFER("deleted_offer"),

  @JsonProperty("new_kmu_consulting_request")
  NEW_KMU_CONSULTING_REQUEST("new_kmu_consulting_request"),

  @JsonProperty("new_application")
  NEW_APPLICATION("new_application"),
  @JsonProperty("updated_application")
  UPDATED_APPLICATION("updated_application"),
  @JsonProperty("deleted_application")
  DELETED_APPLICATION("deleted_application"),

  @JsonProperty("new_user")
  NEW_USER("new_user"),

  @JsonProperty("new_request")
  NEW_REQUEST("new_request"),

  @JsonProperty("new_appointment")
  NEW_APPOINTMENT("new_appointment"),
  @JsonProperty("updated_appointment")
  UPDATED_APPOINTMENT("updated_appointment"),
  @JsonProperty("deleted_appointment")
  DELETED_APPOINTMENT("deleted_appointment"),
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
