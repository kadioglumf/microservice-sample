package com.kadioglumf.authservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum GenderEnum {
  @JsonProperty("male")
  MALE("male"),
  @JsonProperty("female")
  FEMALE("female");

  private final String value;

  GenderEnum(String value) {
    this.value = value;
  }

  public boolean equalsValue(String otherValue) {
    return value.equals(otherValue);
  }

  public String toString() {
    return this.value;
  }

  public static GenderEnum toAttribute(String value) {
    return Arrays.stream(GenderEnum.values())
        .filter(op -> op.toString().equals(value))
        .findFirst()
        .orElse(null);
  }
}
