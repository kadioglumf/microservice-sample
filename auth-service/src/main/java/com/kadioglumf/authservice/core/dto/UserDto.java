package com.kadioglumf.authservice.core.dto;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import java.util.List;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements BaseDto {
  private Long id;
  private String name;
  private String email;
  private Set<RoleTypeEnum> roles;
  private List<PermissionDto> permissions;
}
