package com.kadioglumf.authservice.constant;

public class RedisCacheValueConstants {
  public static final String PREFIX = "auth-service::";
  public static final String GROUPED_PERMISSIONS = PREFIX + "GROUPED_PERMISSIONS";
  public static final String USER_PERMISSIONS = PREFIX + "USER_PERMISSIONS";
  public static final String LOGOUT_JWT_CACHE_KEY = "gateway-service::LOGOUT_JWT";
}
