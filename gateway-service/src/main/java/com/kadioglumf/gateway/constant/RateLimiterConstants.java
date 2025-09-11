package com.kadioglumf.gateway.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RateLimiterConstants {
  public static final String CONFIGURATION_PROPERTY_NAME = "microservice-sample-redis-rate-limiter";

  /** Remaining Rate Limit header name. */
  public static final String REMAINING_HEADER = "X-RateLimit-Remaining";

  /** Replenish Rate Limit header name. */
  public static final String REPLENISH_RATE_HEADER = "X-RateLimit-Replenish-Rate";

  /** Burst Capacity header name. */
  public static final String BURST_CAPACITY_HEADER = "X-RateLimit-Burst-Capacity";

  /** Requested Tokens header name. */
  public static final String REQUESTED_TOKENS_HEADER = "X-RateLimit-Requested-Tokens";
}
