package com.kadioglumf.authservice.constant;

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

  public static final String USER_NOT_FOUND = "AUTH001";
  public static final String USER_LOGIN_PROVIDER_MISMATCH = "AUTH002";
  public static final String REFRESH_TOKEN_NOT_FOUND = "AUTH003";
  public static final String AUTHENTICATION_CREDENTIALS_NOT_FOUND = "AUTH004";
  public static final String REFRESH_TOKEN_EXPIRED = "AUTH005";
  public static final String REGISTER_EMAIL_NOT_AVAILABLE_ERROR = "AUTH006";
  public static final String REGISTER_INVALID_CAPTCHA_ERROR = "AUTH007";
  public static final String TOKEN_NOT_FOUND_ERROR = "AUTH008";
  public static final String TOKEN_INVALID_ERROR = "AUTH009";
  public static final String TOKEN_RESOLVE_ERROR = "AUTH010";
  public static final String AUTHENTICATION_CREDENTIALS_NOT_FOUND_ERROR = "AUTH011";
  public static final String EMAIL_CAN_NOT_EQUALS_ERROR = "AUTH012";
  public static final String EMAIL_EXIST_ERROR = "AUTH013";
  public static final String PASSWORD_NOT_MATCH_ERROR = "AUTH014";
  public static final String PHONE_NUMBER_EXISTS_ERROR = "AUTH015";
  public static final String CONSULT_NOT_FOUND_ERROR = "AUTH016";
  public static final String FILTER_TYPE_NOT_SUITABLE_ERROR = "AUTH017";
  public static final String SEARCH_UNKNOWN_ERROR = "AUTH018";
  public static final String FILTER_PARAMETER_CANNOT_NULL_ERROR = "AUTH019";
  public static final String FILTER_PARAMETER_BLACKLIST_ERROR = "AUTH020";
  public static final String FORBIDDEN_ERROR = "AUTH021";
  public static final String REGISTRATION_APPLICATION_NOT_FOUND_ERROR = "AUTH022";
  public static final String DATE_FORMAT_NOT_FOUND_ERROR = "AUTH023";
  public static final String LOCAL_DATE_PARSE_ERROR = "AUTH024";
  public static final String NUMBER_PARSE_ERROR = "AUTH025";
  public static final String MISSING_PERMISSION_ERROR = "AUTH026";
}
