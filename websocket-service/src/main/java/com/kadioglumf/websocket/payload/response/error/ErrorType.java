package com.kadioglumf.websocket.payload.response.error;

import lombok.Getter;

@Getter
public enum ErrorType {

  // microservice error types
  AUTH_SERVICE_ERROR(1001, "Unknown auth service exception", 500),

  UNKNOWN_WEBSOCKET_SERVICE_ERROR(14001, "Unknown websocket service exception", 500),
  REQUEST_OBJECT_NOT_VALID_ERROR(14002, "Request object not valid", 400),
  FILTER_PARAMETER_BLACKLIST_ERROR(14003, "Blacklist filter parameter", 400),
  FILTER_PARAMETER_CANNOT_NULL_ERROR(14004, "Filter parameter cannot be null.", 400),
  FILTER_TYPE_NOT_SUITABLE_ERROR(14005, "Cannot search with this filter type and field type.", 400),
  SEARCH_UNKNOWN_ERROR(14006, "Unknow search error", 500),
  DATE_FORMAT_NOT_FOUND_ERROR(14007, "Date format not found.", 400),
  LOCAL_DATE_PARSE_ERROR(14008, "LocalDate parse exception.", 400),
  NUMBER_PARSE_ERROR(14009, "Number parse error", 500),
  FORBIDDEN_ERROR(14010, "You cannot reach this resource!", 403),
  WEB_SOCKET_ERROR(14011, "Websocket error!", 400),
  ACTION_TYPE_NOT_FOUND_ERROR(14012, "Action type not found!", 400),
  CHANNEL_NOT_FOUND_ERROR(14013, "Channel not found!", 400),

// External service error types
;

  private final int code;
  private final String description;
  private final int httpStatusCode;

  ErrorType(int code, String description, int httpStatusCode) {
    this.code = code;
    this.description = description;
    this.httpStatusCode = httpStatusCode;
  }

  @Override
  public String toString() {
    return code + ": " + description;
  }
}
