package com.kadioglumf.authservice.core.secure;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecureGroup {
  Secures[] value() default {};
}
