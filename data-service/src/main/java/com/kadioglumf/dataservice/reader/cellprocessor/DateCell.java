package com.kadioglumf.dataservice.reader.cellprocessor;

import com.kadioglumf.dataservice.reader.util.DateUtils;

public class DateCell implements CellProcessor {

  @Override
  public Object execute(Object var1, Class<?> targetType) {
    if (var1 == null) {
      return null;
    }
    return DateUtils.parseStringToDateFormatted(targetType, String.valueOf(var1));
  }
}
