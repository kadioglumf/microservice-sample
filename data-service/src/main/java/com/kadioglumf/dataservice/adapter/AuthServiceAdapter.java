package com.kadioglumf.dataservice.adapter;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.constant.RedisCacheValueConstants;
import com.kadioglumf.dataservice.core.dto.UserDto;
import com.kadioglumf.dataservice.core.exception.ApplicationException;
import com.kadioglumf.dataservice.payload.request.GetUserDetailsRequest;
import com.kadioglumf.dataservice.payload.response.UserDetailsResponse;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class AuthServiceAdapter {

  private final RestClient restClient;

  private final String BASE_URL = "http://auth-service/auth-service";

  @Cacheable(
      value = RedisCacheValueConstants.FIVE_MINUTE_CACHE_KEY,
      key = "T(com.kadioglumf.dataservice.util.DataCacheKeyUtils).sortedUserIdsKey(#userIds)")
  public Set<UserDetailsResponse> getUserDetailsByUserIds(Set<Long> userIds) {
    GetUserDetailsRequest request = new GetUserDetailsRequest();
    request.setUserIds(userIds);

    String url = BASE_URL + "/auth/getUserDetailsByUserIds";
    ResponseEntity<Set<UserDetailsResponse>> responseEntity =
        restClient
            .post()
            .uri(url)
            // .attributes(clientRegistrationId(ServiceConstants.API_COMMUNICATION))
            // .headers(h -> h.addAll(AppUtils.getHttpHeader()))
            .body(request)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {});

    if (responseEntity.getBody() == null || HttpStatus.OK != responseEntity.getStatusCode()) {
      throw new ApplicationException(ExceptionConstants.HTTP_RESPONSE_NOT_VALID);
    }
    return responseEntity.getBody();
  }

  // TODO email değiştirildiğinde cache güncellenmeli. süre arttırılabilir
  @Cacheable(
      value = RedisCacheValueConstants.FIVE_MINUTE_CACHE_KEY,
      key = "{#root.methodName, #email}")
  public UserDto findByEmail(String email) {
    return restClient
        .get()
        .uri(
            BASE_URL,
            uriBuilder -> uriBuilder.path("/auth/findByEmail").queryParam("email", email).build())
        // .attributes(clientRegistrationId(ServiceConstants.API_COMMUNICATION))
        .retrieve()
        .toEntity(UserDto.class)
        .getBody();
  }
}
