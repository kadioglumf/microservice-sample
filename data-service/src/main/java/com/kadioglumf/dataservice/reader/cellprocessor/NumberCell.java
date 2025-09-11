package com.kadioglumf.dataservice.reader.cellprocessor;

import com.kadioglumf.dataservice.reader.util.NumberUtils;
import java.text.ParseException;

public class NumberCell implements CellProcessor {

  @Override
  public Object execute(Object var1, Class<?> targetType) {
    if (var1 == null) {
      return null;
    }

    try {
      return NumberUtils.parseNumericValue(String.valueOf(var1), targetType);
    } catch (ParseException ex) {
      throw new RuntimeException(String.format("cannot parse: %s", var1));
    }
  }
}
