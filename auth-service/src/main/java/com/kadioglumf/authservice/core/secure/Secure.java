package com.kadioglumf.authservice.core.secure;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import java.lang.annotation.*;

@Repeatable(Secures.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {
  String[] permissions() default {};

  RoleTypeEnum[] role() default {};
}
