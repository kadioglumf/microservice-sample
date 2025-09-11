package com.kadioglumf.websocket.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstants {
  private static final String PREFIX = "websocket-service::";
  public static final String CHANNEL_CACHE_VALUE = PREFIX + "channels";
  public static final String CHANNEL_CACHE_KEY = "channel-list";

  public static final String NOTIFICATION_CACHE_VALUE = PREFIX + "notifications";

  public static final String USER_CHANNEL_PREFERENCES_CACHE_VALUE =
      PREFIX + "websocket-user-channel-preferences";
  public static final String USER_ID_KEY = "userId";
  public static final String CHANNEL_NAME_KEY = "chanelName";

  public static final String USER_DETAILS_CACHE_VALUE = PREFIX + "websocket-user-details";
  public static final String ROLE_KEY = "role";
}
