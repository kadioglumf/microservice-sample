package com.kadioglumf.gateway.config;

import java.util.List;
import java.util.Optional;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfiguration {

  @Bean
  @SuppressWarnings("unchecked")
  public RedisScript<List<Long>> redisRequestRateLimiterScript() {
    DefaultRedisScript redisScript = new DefaultRedisScript<>();
    redisScript.setScriptSource(
        new ResourceScriptSource(new ClassPathResource("scripts/rate_limiter.lua")));
    redisScript.setResultType(List.class);
    return redisScript;
  }

  /*  @Bean
  public RedisRateLimiter redisRateLimiter(
      ReactiveStringRedisTemplate redisTemplate,
      @Qualifier("redisRequestRateLimiterScript1") RedisScript<List<Long>> redisScript,
      ConfigurationService configurationService) {
    return new RedisRateLimiter(redisTemplate, redisScript, configurationService);
  }*/
  /*
  @Bean
  public CustomRedisRateLimiter customRedisRateLimiter(
          ReactiveStringRedisTemplate redisTemplate,
          @Qualifier("redisRequestRateLimiterScript1") RedisScript<List<Long>> redisScript,
          ConfigurationService configurationService) {
    return new CustomRedisRateLimiter(redisTemplate, redisScript, configurationService);
  }*/

  @Bean
  public KeyResolver userKeyResolver() {
    return exchange ->
        Optional.ofNullable(exchange.getRequest().getRemoteAddress())
            .map(address -> address.getAddress().getHostAddress())
            .map(Mono::just)
            .orElseGet(Mono::empty);
  }
}
