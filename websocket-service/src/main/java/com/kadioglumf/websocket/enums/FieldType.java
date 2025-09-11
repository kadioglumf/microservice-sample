package com.kadioglumf.websocket.enums;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.util.DateUtils;
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
        throw new BusinessException(ExceptionConstants.NUMBER_PARSE_ERROR);
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
