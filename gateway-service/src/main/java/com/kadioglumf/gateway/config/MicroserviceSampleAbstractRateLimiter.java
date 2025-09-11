package com.kadioglumf.gateway.config;

import static com.kadioglumf.gateway.constant.RedisCacheValueConstants.REQUEST_RATE_LIMIT_CACHE_KEY;

import com.kadioglumf.gateway.constant.RateLimiterConstants;
import jakarta.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
public abstract class MicroserviceSampleAbstractRateLimiter
    extends AbstractRateLimiter<MicroserviceSampleAbstractRateLimiter.Config> {
  protected final ReactiveStringRedisTemplate redisTemplate;

  /** Whether or not to include headers containing rate limiter information, defaults to true. */
  private boolean includeHeaders = true;

  protected MicroserviceSampleAbstractRateLimiter(
      Class<MicroserviceSampleAbstractRateLimiter.Config> configClass,
      ReactiveStringRedisTemplate redisTemplate,
      ConfigurationService configurationService) {
    super(configClass, RateLimiterConstants.CONFIGURATION_PROPERTY_NAME, configurationService);
    this.redisTemplate = redisTemplate;
  }

  protected Config loadConfiguration(String routeId) {
    var routeConfig = getConfig().get(routeId);

    if (routeConfig == null) {
      routeConfig = getConfig().get(RouteDefinitionRouteLocator.DEFAULT_FILTERS);
    }

    if (routeConfig == null) {
      throw new IllegalArgumentException(
          "No Configuration found for route " + routeId + " or defaultFilters");
    }
    return routeConfig;
  }

  protected Map<String, String> getHeaders(Config config, Long tokensLeft) {
    Map<String, String> headers = new HashMap<>();
    if (isIncludeHeaders()) {
      headers.put(RateLimiterConstants.REMAINING_HEADER, tokensLeft.toString());
      headers.put(RateLimiterConstants.REPLENISH_RATE_HEADER, String.valueOf(config.getTtl()));
      headers.put(
          RateLimiterConstants.BURST_CAPACITY_HEADER, String.valueOf(config.getBurstCapacity()));
      headers.put(
          RateLimiterConstants.REQUESTED_TOKENS_HEADER,
          String.valueOf(config.getRequestedTokens()));
    }
    return headers;
  }

  protected static String getPrefix(String id, String routeId) {
    return REQUEST_RATE_LIMIT_CACHE_KEY + "{" + id + "}" + "." + "{" + routeId + "}";
  }

  protected static String getPrefixWithoutRouteId(String id) {
    return REQUEST_RATE_LIMIT_CACHE_KEY + "{" + id + "}";
  }

  protected static String getKey(String id, String routeId, String key) {
    return null == routeId
        ? getPrefixWithoutRouteId(id) + "." + key
        : getPrefix(id, routeId) + "." + key;
  }

  @Validated
  @Getter
  @Setter
  public static class Config {

    @Min(1)
    private int ttl;

    @Min(0)
    private int burstCapacity = 1;

    @Min(1)
    private int requestedTokens = 1;

    @Min(1)
    private int blockedTtl;

    @Min(1)
    private int blockLimit;

    @Override
    public String toString() {
      return new ToStringCreator(this)
          .append("replenishRate", ttl)
          .append("burstCapacity", burstCapacity)
          .append("requestedTokens", requestedTokens)
          .append("blockedTtl", blockedTtl)
          .append("blockLimit", blockLimit)
          .toString();
    }
  }
}
