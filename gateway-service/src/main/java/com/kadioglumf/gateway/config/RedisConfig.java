package com.kadioglumf.gateway.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class RedisConfig {

  @Value("${redis.host}")
  private String redisHostName;

  @Value("${redis.port}")
  private Integer redisPort;

  @Value("${redis.password}")
  private String redisPassword;

  @Value("${redis.socketTimeout}")
  private long socketTimeout;

  @Value("${redis.commandTimeout}")
  private long redisCommandTimeout;

  @Value("${redis.database}")
  private int database;

  @Bean
  LettuceConnectionFactory lettuceConnectionFactory() {

    final SocketOptions socketOptions =
        SocketOptions.builder().connectTimeout(Duration.ofMillis(socketTimeout)).build();
    final ClientOptions clientOptions =
        ClientOptions.builder().socketOptions(socketOptions).build();

    LettuceClientConfiguration clientConfig =
        LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofMillis(redisCommandTimeout))
            .clientOptions(clientOptions)
            .build();
    RedisStandaloneConfiguration serverConfig =
        new RedisStandaloneConfiguration(redisHostName, redisPort);
    serverConfig.setPassword(redisPassword);
    serverConfig.setDatabase(database);

    final LettuceConnectionFactory lettuceConnectionFactory =
        new LettuceConnectionFactory(serverConfig, clientConfig);
    lettuceConnectionFactory.setValidateConnection(true);
    return new LettuceConnectionFactory(serverConfig, clientConfig);
  }

  @Bean
  public CacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .cacheDefaults(createCacheConfiguration(null))
        .build();
  }

  private RedisCacheConfiguration createCacheConfiguration(Duration duration) {
    var config =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()));
    if (duration != null) {
      config.entryTtl(duration);
    }
    return config;
  }
}
