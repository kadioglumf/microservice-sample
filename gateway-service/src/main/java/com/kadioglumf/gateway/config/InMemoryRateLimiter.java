package com.kadioglumf.gateway.config;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
@Getter
@Setter
public class InMemoryRateLimiter extends MicroserviceSampleAbstractRateLimiter {
  private static final ConcurrentHashMap<String, RateLimitInfo> rateLimitMap =
      new ConcurrentHashMap<>();

  private AtomicBoolean initialized = new AtomicBoolean(false);

  public InMemoryRateLimiter(
      ReactiveStringRedisTemplate redisTemplate, ConfigurationService configurationService) {
    super(RedisRateLimiter.Config.class, redisTemplate, configurationService);
    this.initialized.compareAndSet(false, true);
  }

  private RateLimitInfo getRateLimitInfo(String id, String routeId, long burstCapacity) {
    return rateLimitMap.computeIfAbsent(
        getPrefix(id, routeId), k -> RateLimitInfo.defaultRateLimitInfo(burstCapacity, routeId));
  }

  private void updateRateLimitInfo(String id, String routeId, RateLimitInfo rateLimitInfo) {
    rateLimitMap.put(getPrefix(id, routeId), rateLimitInfo);
  }

  /**
   * This uses a basic token bucket algorithm and relies on the fact that Redis scripts execute
   * atomically. No other operations can run between fetching the count and writing the new count.
   */
  @Override
  @SuppressWarnings("unchecked")
  public Mono<Response> isAllowed(String routeId, String id) {
    if (!this.initialized.get()) {
      throw new IllegalStateException("InMemoryRateLimiter is not initialized");
    }

    var routeConfig = loadConfiguration(routeId);

    try {
      long now = System.currentTimeMillis() / 1000;
      var rateLimitInfo = getRateLimitInfo(id, routeId, routeConfig.getBurstCapacity());

      if (rateLimitInfo.getBlockCount() >= routeConfig.getBlockLimit()) {
        if (now > rateLimitInfo.getBlockedTtl()) {
          rateLimitInfo =
              RateLimitInfo.defaultRateLimitInfo(routeConfig.getBurstCapacity(), routeId);
          updateRateLimitInfo(id, routeId, rateLimitInfo);
        } else {
          return Mono.just(new Response(false, getHeaders(routeConfig, -1L)));
        }
      }

      var delta = Math.max(0, now - rateLimitInfo.getTimestamp());
      var filledTokens = Math.min(routeConfig.getBurstCapacity(), rateLimitInfo.getToken() + delta);
      var allowed = filledTokens >= routeConfig.getRequestedTokens();
      var tokensLeft = filledTokens;

      if (allowed) {
        tokensLeft = filledTokens - routeConfig.getRequestedTokens();
      } else {
        rateLimitInfo.setBlockCount(rateLimitInfo.getBlockCount() + 1);
        rateLimitInfo.setTimestamp(now);
        if (rateLimitInfo.getBlockCount() >= routeConfig.getBlockLimit()) {
          rateLimitInfo.setBlockedTtl(now + routeConfig.getBlockedTtl());
        }
      }

      rateLimitInfo.setToken(tokensLeft);
      rateLimitInfo.setTimestamp(now);

      return Mono.just(new Response(allowed, getHeaders(routeConfig, tokensLeft)));
    } catch (Exception e) {
      log.error("Error determining if user allowed from redis", e);
    }
    return Mono.just(new Response(true, getHeaders(routeConfig, -1L)));
  }

  public void transferToRedis() {
    rateLimitMap.forEach(
        (key, value) -> {
          var routeConfig = loadConfiguration(value.getRouteId());

          String tokenKey = key + ".tokens";
          redisTemplate
              .opsForValue()
              .get(tokenKey)
              .flatMap(
                  currentValue -> {
                    if (currentValue == null || Long.parseLong(currentValue) > value.getToken()) {
                      return redisTemplate
                          .opsForValue()
                          .set(
                              tokenKey,
                              String.valueOf(value.getToken()),
                              Duration.ofSeconds(routeConfig.getTtl()))
                          .then();
                    }
                    return Mono.empty();
                  })
              .switchIfEmpty(
                  redisTemplate
                      .opsForValue()
                      .set(
                          tokenKey,
                          String.valueOf(value.getToken()),
                          Duration.ofSeconds(routeConfig.getTtl()))
                      .then())
              .subscribe();

          String timestampKey = key + ".timestamp";
          redisTemplate
              .opsForValue()
              .get(timestampKey)
              .flatMap(
                  currentValue -> {
                    if (currentValue == null
                        || Long.parseLong(currentValue) < value.getTimestamp()) {
                      return redisTemplate
                          .opsForValue()
                          .set(
                              timestampKey,
                              String.valueOf(value.getTimestamp()),
                              Duration.ofSeconds(routeConfig.getTtl()))
                          .then();
                    }
                    return Mono.empty();
                  })
              .switchIfEmpty(
                  redisTemplate
                      .opsForValue()
                      .set(
                          timestampKey,
                          String.valueOf(value.getTimestamp()),
                          Duration.ofSeconds(routeConfig.getTtl()))
                      .then())
              .subscribe();

          int lastIndex = key.lastIndexOf(".{");
          String blockedKey = ((lastIndex != -1) ? key.substring(0, lastIndex) : key) + ".blocked";
          redisTemplate
              .opsForValue()
              .get(blockedKey)
              .flatMap(
                  currentValue -> {
                    if (currentValue == null
                        || Long.parseLong(currentValue) < value.getBlockCount()) {
                      return redisTemplate
                          .opsForValue()
                          .set(
                              blockedKey,
                              String.valueOf(value.getBlockCount()),
                              Duration.ofSeconds(routeConfig.getBlockedTtl()))
                          .then();
                    }
                    return Mono.empty();
                  })
              .switchIfEmpty(
                  redisTemplate
                      .opsForValue()
                      .set(
                          blockedKey,
                          String.valueOf(value.getBlockCount()),
                          Duration.ofSeconds(routeConfig.getBlockedTtl()))
                      .then())
              .subscribe();
        });

    rateLimitMap.clear();
  }

  @Getter
  @Setter
  private static class RateLimitInfo {
    long token;
    long blockCount;
    long timestamp;
    long blockedTtl;
    String routeId;

    public RateLimitInfo(
        long token, long blockCount, long timestamp, long blockTtl, String routeId) {
      this.token = token;
      this.blockCount = blockCount;
      this.timestamp = timestamp;
      this.blockedTtl = blockTtl;
      this.routeId = routeId;
    }

    public static RateLimitInfo defaultRateLimitInfo(long burstCapacity, String routeId) {
      return new RateLimitInfo(burstCapacity, 0, 0, 0, routeId);
    }
  }
}
