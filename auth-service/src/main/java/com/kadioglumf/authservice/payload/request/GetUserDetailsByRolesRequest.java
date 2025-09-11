package com.kadioglumf.authservice.payload.request;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsByRolesRequest {
  @NotNull
  @Size(min = 1)
  private Set<RoleTypeEnum> roles;
}
