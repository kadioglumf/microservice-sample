package com.kadioglumf.authservice.util;

import com.kadioglumf.authservice.core.dto.UserDto;

public class UserThreadContext {
  private static final ThreadLocal<UserDto> user = new ThreadLocal<>();
  private static final ThreadLocal<String> jwt = new ThreadLocal<>();

  public static void setUser(UserDto user) {
    UserThreadContext.user.set(user);
  }

  public static UserDto getUser() {
    return user.get();
  }

  public static void setJwt(String jwt) {
    UserThreadContext.jwt.set(jwt);
  }

  public static String getJwt() {
    return jwt.get();
  }

  public static void clear() {
    user.remove();
  }
}
