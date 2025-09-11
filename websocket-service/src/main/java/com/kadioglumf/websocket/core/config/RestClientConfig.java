package com.kadioglumf.websocket.core.config;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.constant.ServiceConstants;
import com.kadioglumf.websocket.core.exception.BusinessException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  @LoadBalanced
  public RestClient.Builder restClientBuilder(
      OAuth2AuthorizedClientManager authorizedClientManager) {
    return RestClient.builder()
        .requestInitializer(
            request -> {
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
              request.getHeaders().setBearerAuth(accessToken);
            });
  }

  @Bean
  public RestClient restClient(RestClient.Builder builder) {
    return builder.build();
  }

  @Bean
  public RestClient externalRestClient() {
    return RestClient.builder().build();
  }
}
