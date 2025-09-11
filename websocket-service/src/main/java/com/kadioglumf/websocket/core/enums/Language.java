package com.kadioglumf.websocket.core.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum Language {
  @JsonProperty("en")
  EN("en"),
  @JsonProperty("de")
  DE("de"),
  @JsonProperty("fr")
  FR("fr"),
  @JsonProperty("it")
  IT("it");

  private final String value;

  Language(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }

  public static Language toAttribute(String value) {
    return Arrays.stream(Language.values())
        .filter(op -> op.getValue().equals(value))
        .findFirst()
        .orElseThrow();
  }
}
