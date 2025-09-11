package com.kadioglumf.authservice.controller;

import com.kadioglumf.authservice.payload.request.*;
import com.kadioglumf.authservice.payload.response.*;
import com.kadioglumf.authservice.constant.ServiceConstants;
import com.kadioglumf.authservice.core.dto.UserDto;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.secure.Secure;
import com.kadioglumf.authservice.payload.request.GetUserDetailsByRolesRequest;
import com.kadioglumf.authservice.payload.request.GetUserDetailsRequest;
import com.kadioglumf.authservice.payload.request.LoginRequest;
import com.kadioglumf.authservice.payload.response.CreateAccessTokenResponse;
import com.kadioglumf.authservice.payload.response.GetUserDetailsResponse;
import com.kadioglumf.authservice.payload.response.LoginResponse;
import com.kadioglumf.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@ResponseBody
public class AuthController {
  private final AuthService authService;

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
    return authService.login(loginRequest);
  }

  @GetMapping("/logout")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN})
  public void logout() {
    authService.logout();
  }

  @GetMapping("/access-token")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN})
  public CreateAccessTokenResponse getAccessToken(
      @RequestHeader(ServiceConstants.REFRESH_TOKEN) String refreshToken) {
    return authService.getAccessTokenUsingRefreshToken(refreshToken);
  }

  @GetMapping("/findByEmail")
  @Operation(summary = "Find User By Email")
  @Secure(role = {RoleTypeEnum.ROLE_SYSTEM})
  public UserDto getUserDto(@RequestParam String email) {
    return authService.getUserDtoByEmail(email);
  }

  @GetMapping("/findById")
  @Operation(summary = "Find User By Id")
  @Secure(role = {RoleTypeEnum.ROLE_SYSTEM})
  public UserDto getUserDto(@RequestParam Long id) {
    return authService.getUserDtoById(id);
  }

  @PostMapping("/getUserDetailsByUserIds")
  @Secure(role = RoleTypeEnum.ROLE_SYSTEM)
  public Set<GetUserDetailsResponse> getUserDetailsByUserIds(
      @RequestBody GetUserDetailsRequest request) {
    return authService.getUserDetailsByUserIds(request);
  }

  @PostMapping("/getUserDetailsByRoles")
  @Secure(role = RoleTypeEnum.ROLE_SYSTEM)
  public Set<GetUserDetailsResponse> getUserDetailsByRoles(
      @RequestBody GetUserDetailsByRolesRequest request) {
    return authService.getUserDetailsByRoles(request);
  }
}
