package com.kadioglumf.websocket.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public enum WsCategoryType {
  @JsonProperty("legal_insurance_offer")
  LEGAL_INSURANCE_OFFER("legal_insurance_offer"),
  @JsonProperty("health_insurance_offer")
  HEALTH_INSURANCE_OFFER("health_insurance_offer"),
  @JsonProperty("asisa_insurance_offer")
  ASISA_INSURANCE_OFFER("asisa_insurance_offer"),
  @JsonProperty("auto_insurance_offer")
  AUTO_INSURANCE_OFFER("auto_insurance_offer"),
  @JsonProperty("household_insurance_offer")
  HOUSEHOLD_INSURANCE_OFFER("household_insurance_offer"),
  @JsonProperty("tcs_insurance_offer")
  TCS_INSURANCE_OFFER("tcs_insurance_offer"),
  @JsonProperty("life_insurance_offer")
  LIFE_INSURANCE_OFFER("life_insurance_offer"),

  @JsonProperty("credit_finance_application")
  CREDIT_FINANCE_APPLICATION("credit_finance_application"),
  @JsonProperty("mortgage_appointment")
  MORTGAGE_APPOINTMENT("mortgage_appointment"),

  @JsonProperty("new_user_registered")
  NEW_USER_REGISTERED("new_user_registered"),

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

  @JsonProperty("kmu_consulting_created")
  KMU_CONSULTING_CREATED("kmu_consulting_created"),
  @JsonProperty("kmu_consulting_assigned")
  KMU_CONSULTING_ASSIGNED("kmu_consulting_assigned"),
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
