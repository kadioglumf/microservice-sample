package com.kadioglumf.gateway.filter;

import com.kadioglumf.gateway.constant.RedisCacheValueConstants;
import com.kadioglumf.gateway.constant.ServiceConstants;
import com.kadioglumf.gateway.error.RestResponseError;
import com.kadioglumf.gateway.utils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter
    extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final ReactiveStringRedisTemplate redisTemplate;

  public JwtAuthenticationFilter(ReactiveStringRedisTemplate redisTemplate) {
    super(Config.class);
    this.redisTemplate = redisTemplate;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return ((exchange, chain) -> {
      if (exchange.getRequest().getHeaders().containsKey(ServiceConstants.TOKEN_HEADER)) {
        var authHeaders = exchange.getRequest().getHeaders().get(ServiceConstants.TOKEN_HEADER);

        if (CollectionUtils.isEmpty(authHeaders)) {
          log.error("Authorization header is invalid at {}", exchange.getRequest().getURI());
          return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        }

        var token =
            authHeaders.getFirst().startsWith("Bearer ")
                ? authHeaders.getFirst().replace(ServiceConstants.TOKEN_PREFIX, "")
                : authHeaders.getFirst();

        return isKeyExists(token)
            .flatMap(
                isValid -> {
                  if (!isValid) {
                    log.error(
                        "Authorization token is invalid at {}", exchange.getRequest().getURI());
                    return onError(
                        exchange, "Authorization token is invalid", HttpStatus.UNAUTHORIZED);
                  }
                  return chain.filter(exchange);
                })
            .onErrorResume(
                e -> {
                  log.error("Token validation failed : {}", e.getMessage());
                  return onError(exchange, e.getMessage(), HttpStatus.UNAUTHORIZED);
                });
      }
      return chain.filter(exchange);
    });
  }

  private Mono<Boolean> isKeyExists(String jwt) {
    return redisTemplate
        .hasKey(RedisCacheValueConstants.LOGOUT_JWT_CACHE_KEY + jwt)
        .flatMap(
            isBlacklisted -> {
              if (Boolean.TRUE.equals(isBlacklisted)) {
                return Mono.just(false);
              }
              return Mono.just(true);
            });
  }

  private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {

    exchange.getResponse().setStatusCode(httpStatus);
    exchange.getResponse().getHeaders().add("Content-Type", "application/json");

    var body = RestResponseError.builder().errorCode("(APP)CORE00007").errorMessage(error).build();

    DataBuffer dataBuffer =
        exchange.getResponse().bufferFactory().wrap(ConvertUtils.writeValueAsBytes(body));
    return exchange.getResponse().writeWith(Mono.just(dataBuffer));
  }

  public static class Config {}
}
