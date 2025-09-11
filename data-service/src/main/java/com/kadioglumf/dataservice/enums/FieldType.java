package com.kadioglumf.dataservice.enums;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.util.DateUtils;
import org.apache.commons.lang.math.NumberUtils;

public enum FieldType {
  BOOLEAN {
    public Object parse(String value) {
      return Boolean.valueOf(value);
    }
  },

  CHAR {
    public Object parse(String value) {
      return value.charAt(0);
    }
  },

  DATE {
    public Object parse(String value) {
      return DateUtils.parseStringToObjectByFormat(value);
    }
  },

  NUMBER {
    public Object parse(String value) {
      try {
        return NumberUtils.createNumber(value);
      } catch (NumberFormatException ex) {
        throw new BusinessException(ExceptionConstants.NUMBER_PARSE_ERROR, ex.getMessage());
      }
    }
  },

  STRING {
    public Object parse(String value) {
      return value;
    }
  };

  public abstract Object parse(String value);
}
