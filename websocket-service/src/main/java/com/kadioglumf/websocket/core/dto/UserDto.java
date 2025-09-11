package com.kadioglumf.websocket.core.dto;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import java.util.List;
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
  private RoleTypeEnum role;
  private List<PermissionDto> permissions;
}
