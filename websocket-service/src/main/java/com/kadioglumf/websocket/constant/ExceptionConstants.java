package com.kadioglumf.websocket.constant;

public class ExceptionConstants {
  public static final String APPLICATION_ERROR_PREFIX = "(APP)";
  public static final String BUSINESS_ERROR_PREFIX = "(BUS)";

  public static final String UNEXPECTED = "CORE00001";
  public static final String ERROR_REST_REQUEST_NOT_PROPER = "CORE00002";
  public static final String FILE_UPLOAD_SIZE_LIMIT = "CORE00003";
  public static final String HTTP_RESPONSE_NOT_VALID = "CORE00004";
  public static final String IO_EXCEPTION = "CORE00005";
  public static final String NO_RESOURCE_FOUND = "CORE00006";
  public static final String UNAUTHORIZED = "CORE00007";
  public static final String METHOD_ARGUMENT_NOT_VALID = "CORE00008";

  public static final String SERVER_STARTUP_EXCEPTION = "CORE10000";
  public static final String REQUEST_OBJECT_NOT_VALID_ERROR = "WS001";
  public static final String FILTER_PARAMETER_BLACKLIST_ERROR = "WS002";
  public static final String FILTER_PARAMETER_CANNOT_NULL_ERROR = "WS003";
  public static final String FILTER_TYPE_NOT_SUITABLE_ERROR = "WS004";
  public static final String SEARCH_UNKNOWN_ERROR = "WS005";
  public static final String DATE_FORMAT_NOT_FOUND_ERROR = "WS006";
  public static final String LOCAL_DATE_PARSE_ERROR = "WS007";
  public static final String NUMBER_PARSE_ERROR = "WS008";
  public static final String FORBIDDEN_ERROR = "WS009";
  public static final String WEB_SOCKET_ERROR = "WS010";
  public static final String ACTION_TYPE_NOT_FOUND_ERROR = "WS011";
  public static final String CHANNEL_NOT_FOUND_ERROR = "WS012";
}
