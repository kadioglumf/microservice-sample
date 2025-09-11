package com.kadioglumf.dataservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum Gender {
  @JsonProperty("male")
  MALE("male"),
  @JsonProperty("female")
  FEMALE("female");

  private final String value;

  private Gender(String value) {
    this.value = value;
  }

  public boolean equalsValue(String otherValue) {
    return value.equals(otherValue);
  }

  public String toString() {
    return this.value;
  }

  public static Gender toAttribute(String value) {
    return Arrays.stream(Gender.values())
        .filter(op -> op.toString().equals(value))
        .findFirst()
        .orElse(null);
  }

  public String getValue() {
    return this.value;
  }
}
