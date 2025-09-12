package com.kadioglumf.dataservice.util;

import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
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

}
