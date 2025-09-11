package com.kadioglumf.websocket.core.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto implements BaseDto {
  private Long id;
  private String authorizationName;
  private String permission;
}
