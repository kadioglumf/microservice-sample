package com.kadioglumf.dataservice.reader.annotations.csv;

import com.kadioglumf.dataservice.reader.cellprocessor.CellProcessor;
import com.kadioglumf.dataservice.reader.cellprocessor.StringCell;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CsvColumn {
  int columnIndex() default -1;

  String columnName() default "";

  Class<? extends CellProcessor> cellProcessor() default StringCell.class;
}
