package com.kadioglumf.websocket.core.secure;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secures {
  Secure[] value() default {};

  boolean isAnyRole() default true;
}
