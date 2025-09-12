package com.kadioglumf.authservice.controller;

import com.kadioglumf.authservice.payload.request.*;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.secure.Secure;
import com.kadioglumf.authservice.payload.request.SignupRequest;
import com.kadioglumf.authservice.payload.response.MessageResponse;
import com.kadioglumf.authservice.payload.response.UserDetailsResponse;
import com.kadioglumf.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@ResponseBody
public class UserController {
  private final UserService userService;

  @PostMapping("/register")
  public MessageResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    userService.registerUser(signUpRequest);
    return new MessageResponse("User registered successfully!");
  }

  @GetMapping("/getDetails")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN, RoleTypeEnum.ROLE_MODERATOR})
  @Operation(summary = "Get Authenticated User Details")
  public UserDetailsResponse getUserDetails() {
    return userService.getUserDetails();
  }

  @PostMapping("/updateUserDetails")
  @Secure(role = {RoleTypeEnum.ROLE_USER})
  public MessageResponse updateUserDetails() {
    userService.updateUserDetails();
    return new MessageResponse("User details updated successfully!");
  }
}
