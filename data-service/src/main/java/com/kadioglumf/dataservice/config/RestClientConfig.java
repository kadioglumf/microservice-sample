package com.kadioglumf.dataservice.config;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.constant.ServiceConstants;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.util.AppUtils;
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
              request.getHeaders().addAll(AppUtils.getHttpHeader());
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
