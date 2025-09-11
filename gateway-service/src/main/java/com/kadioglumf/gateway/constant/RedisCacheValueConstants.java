package com.kadioglumf.gateway.constant;

public class RedisCacheValueConstants {
  public static final String PREFIX = "gateway-service::";
  public static final String LOGOUT_JWT_CACHE_KEY = PREFIX + "LOGOUT_JWT::";
  public static final String REQUEST_RATE_LIMIT_CACHE_KEY = PREFIX + "REQUEST_RATE_LIMITER::";
}
