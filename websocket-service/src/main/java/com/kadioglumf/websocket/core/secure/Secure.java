package com.kadioglumf.websocket.core.secure;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import java.lang.annotation.*;

@Repeatable(Secures.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {
  String[] permissions() default {};

  RoleTypeEnum[] role() default {};
}
