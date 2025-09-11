package com.kadioglumf.dataservice.reader.annotations.excel;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportExcelSettings {
  boolean isFirstRowHeader() default true;
}
