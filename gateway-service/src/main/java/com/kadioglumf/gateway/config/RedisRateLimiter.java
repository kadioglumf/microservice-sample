package com.kadioglumf.gateway.config;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.cloud.gateway.support.ConfigurationService;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
@Getter
@Setter
@Primary
public class RedisRateLimiter extends MicroserviceSampleAbstractRateLimiter {
  private final RedisScript<List<Long>> redisRequestRateLimiterScript;
  private final InMemoryRateLimiter inMemoryRateLimiter;
  private final ReactiveCircuitBreaker circuitBreaker;

  private AtomicBoolean initialized = new AtomicBoolean(false);

  private boolean isRedisUp = true;

  public RedisRateLimiter(
      ReactiveStringRedisTemplate redisTemplate,
      RedisScript<List<Long>> redisRequestRateLimiterScript,
      ConfigurationService configurationService,
      InMemoryRateLimiter inMemoryRateLimiter,
      ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
    super(RedisRateLimiter.Config.class, redisTemplate, configurationService);
    this.redisRequestRateLimiterScript = redisRequestRateLimiterScript;
    this.inMemoryRateLimiter = inMemoryRateLimiter;
    this.circuitBreaker = circuitBreakerFactory.create("rateLimiterCircuitBreaker");
    this.initialized.compareAndSet(false, true);
  }

  static List<String> getKeys(String id, String routeId) {
    String tokenKey = getKey(id, routeId, "tokens");
    String timestampKey = getKey(id, routeId, "timestamp");
    String blockedKey = getKey(id, null, "blocked");

    return Arrays.asList(tokenKey, timestampKey, blockedKey);
  }

  /**
   * This uses a basic token bucket algorithm and relies on the fact that Redis scripts execute
   * atomically. No other operations can run between fetching the count and writing the new count.
   */
  @Override
  @SuppressWarnings("unchecked")
  public Mono<Response> isAllowed(String routeId, String id) {
    if (!this.initialized.get()) {
      throw new IllegalStateException("RedisRateLimiter is not initialized");
    }

    RedisRateLimiter.Config routeConfig = loadConfiguration(routeId);

    int ttl = routeConfig.getTtl();
    int burstCapacity = routeConfig.getBurstCapacity();
    int requestedTokens = routeConfig.getRequestedTokens();
    int blockedTtl = routeConfig.getBlockedTtl();
    int blockLimit = routeConfig.getBlockLimit();

    List<String> keys = getKeys(id, routeId);

    List<String> scriptArgs =
        Arrays.asList(
            ttl + "",
            burstCapacity + "",
            "",
            requestedTokens + "",
            blockedTtl + "",
            blockLimit + "");

    return circuitBreaker.run(
        this.redisTemplate
            .execute(this.redisRequestRateLimiterScript, keys, scriptArgs)
            .reduce(
                new ArrayList<Long>(),
                (longs, l) -> {
                  longs.addAll(l);
                  return longs;
                })
            .map(
                results -> {
                  if (!isRedisUp) {
                    inMemoryRateLimiter.transferToRedis();
                    isRedisUp = true;
                  }
                  boolean allowed = results.get(0) == 1L;
                  Long tokensLeft = results.get(1);
                  return new Response(allowed, getHeaders(routeConfig, tokensLeft));
                }),
        throwable -> {
          log.error(
              "Redis erişim hatası: {}, InMemoryRateLimiter devreye giriyor.",
              throwable.getMessage());
          isRedisUp = false;
          return inMemoryRateLimiter.isAllowed(routeId, id);
        });
  }
}
