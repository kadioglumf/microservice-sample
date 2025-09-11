package com.kadioglumf.dataservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum WsCategoryType {
  @JsonProperty("contact_request_created")
  CONTACT_REQUEST_CREATED("contact_request_created"),
  @JsonProperty("contact_request_assigned")
  CONTACT_REQUEST_ASSIGNED("contact_request_assigned"),
  @JsonProperty("contact_request_deleted")
  CONTACT_REQUEST_DELETED("contact_request_deleted"),

  @JsonProperty("consulting_request_created")
  CONSULTING_REQUEST_CREATED("consulting_request_created"),
  @JsonProperty("consulting_request_assigned")
  CONSULTING_REQUEST_ASSIGNED("consulting_request_assigned"),
  @JsonProperty("consulting_request_deleted")
  CONSULTING_REQUEST_DELETED("consulting_request_deleted"),
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
