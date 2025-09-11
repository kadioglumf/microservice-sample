package com.kadioglumf.authservice.controller;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.secure.Secure;
import com.kadioglumf.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
@ResponseBody
public class AdminController {
  private final AuthService authService;

  @GetMapping("/countUsers")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  @Operation(summary = "Count number of Users")
  public Long countUsers() {
    return authService.countUsers();
  }
}
