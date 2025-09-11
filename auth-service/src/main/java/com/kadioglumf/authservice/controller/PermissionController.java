package com.kadioglumf.authservice.controller;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.secure.Secure;
import com.kadioglumf.authservice.payload.response.PermissionResponse;
import com.kadioglumf.authservice.service.PermissionService;
import com.kadioglumf.authservice.util.UserThreadContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@Validated
@ResponseBody
public class PermissionController {
  private final PermissionService permissionService;

  @GetMapping("/all")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public List<PermissionResponse> getPermissions() {
    return permissionService.getPermissions();
  }

  @GetMapping("/list-by-user-id")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public List<PermissionResponse> getUserPermissions(@RequestParam Long userId) {
    return permissionService.getUserPermissions(userId);
  }

  @GetMapping("/users-list")
  @Secure(role = {RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_USER})
  public List<PermissionResponse> getUserPermissions() {
    return permissionService.getUserPermissions(UserThreadContext.getUser().getId());
  }
}
