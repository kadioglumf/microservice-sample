package com.kadioglumf.dataservice.core.secure;

import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
import java.lang.annotation.*;

@Repeatable(Secures.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {
  String[] permissions() default {};

  RoleTypeEnum[] role() default {};
}
