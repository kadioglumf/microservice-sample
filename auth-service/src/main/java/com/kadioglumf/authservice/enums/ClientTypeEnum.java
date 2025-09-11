package com.kadioglumf.authservice.enums;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum ClientTypeEnum {
  ADMIN("admin.microservice.sample.com", List.of(RoleTypeEnum.ROLE_ADMIN)),
  PUBLIC_CLIENT(
      "microservice.sample.com", List.of(RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN));

  private final String origin;
  private final List<RoleTypeEnum> allowedRoles;

  ClientTypeEnum(String origin, List<RoleTypeEnum> allowedRoles) {
    this.origin = origin;
    this.allowedRoles = allowedRoles;
  }

  public static ClientTypeEnum fromOrigin(String origin) {
    return Stream.of(ClientTypeEnum.values())
        .filter(clientTypeEnum -> origin.contains(clientTypeEnum.getOrigin()))
        .findFirst()
        .orElse(ClientTypeEnum.PUBLIC_CLIENT);
  }
}
