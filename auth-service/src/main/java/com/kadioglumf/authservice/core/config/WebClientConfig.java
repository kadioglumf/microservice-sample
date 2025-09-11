package com.kadioglumf.authservice.core.config;

import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.constant.ServiceConstants;
import com.kadioglumf.authservice.core.exception.BusinessException;
import com.kadioglumf.authservice.util.AppUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

  @Bean
  @LoadBalanced
  public WebClient.Builder webClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {
    return WebClient.builder()
        .filter(
            (request, next) -> {
              OAuth2AuthorizeRequest authorizeRequest =
                  OAuth2AuthorizeRequest.withClientRegistrationId(
                          ServiceConstants.API_COMMUNICATION)
                      .principal(ServiceConstants.API_COMMUNICATION)
                      .build();

              OAuth2AuthorizedClient client = authorizedClientManager.authorize(authorizeRequest);
              if (client == null) {
                throw new BusinessException(ExceptionConstants.UNAUTHORIZED);
              }

              String accessToken = client.getAccessToken().getTokenValue();

              return next.exchange(
                  ClientRequest.from(request)
                      .headers(
                          headers -> {
                            headers.setBearerAuth(accessToken);
                            headers.addAll(AppUtils.getHttpHeader());
                          })
                      .build());
            });
  }

  @Bean
  public WebClient webClient(WebClient.Builder webClientBuilder) {
    return webClientBuilder.build();
  }
}
