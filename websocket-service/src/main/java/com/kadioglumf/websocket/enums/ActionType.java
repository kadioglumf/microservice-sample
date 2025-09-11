package com.kadioglumf.websocket.enums;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.exception.BusinessException;

public enum ActionType {
  SUBSCRIBE("subscribe"),
  UNSUBSCRIBE("unsubscribe"),
  SEND_MESSAGE("send"),
  REFRESH_CONNECTION("refresh_connection"),
  SUBSCRIBE_ALL("subscribe_all");

  private final String value;

  ActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static ActionType getFromValue(String value) {
    for (ActionType type : values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    throw new BusinessException(ExceptionConstants.ACTION_TYPE_NOT_FOUND_ERROR);
  }
}
