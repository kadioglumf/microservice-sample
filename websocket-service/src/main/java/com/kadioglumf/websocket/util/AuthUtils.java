package com.kadioglumf.websocket.util;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuthUtils {
  public static String getUserEmail() {
    var user = UserThreadContext.getUser();
    if (user == null) {
      return null;
    }
    return UserThreadContext.getUser().getEmail();
  }

  public static Long getUserId() {
    var user = UserThreadContext.getUser();
    if (user == null) {
      return null;
    }
    return user.getId();
  }

  public static boolean isPartner() {
    var user = UserThreadContext.getUser();
    return user != null && RoleTypeEnum.ROLE_PARTNER.equals(user.getRole());
  }

  public static boolean isAdmin() {
    var user = UserThreadContext.getUser();
    return user != null && RoleTypeEnum.ROLE_ADMIN.equals(user.getRole());
  }
}
