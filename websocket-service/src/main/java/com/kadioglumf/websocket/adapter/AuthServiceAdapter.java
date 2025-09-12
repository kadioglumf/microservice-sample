package com.kadioglumf.websocket.adapter;

import com.kadioglumf.websocket.constant.CacheConstants;
import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.constant.RedisCacheValueConstants;
import com.kadioglumf.websocket.core.dto.UserDto;
import com.kadioglumf.websocket.core.exception.ApplicationException;
import com.kadioglumf.websocket.payload.request.adapter.GetUserDetailsByRolesRequest;
import com.kadioglumf.websocket.payload.request.adapter.GetUserDetailsRequest;
import com.kadioglumf.websocket.payload.response.GetUserDetailsResponse;
import com.kadioglumf.websocket.util.AppUtils;
import java.util.List;
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
      value = CacheConstants.USER_DETAILS_CACHE_VALUE,
      key = "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' + #userId")
  public List<GetUserDetailsResponse> getUserDetailsByUserId(Long userId) {
    GetUserDetailsRequest request = new GetUserDetailsRequest();
    request.setUserIds(Set.of(userId));

    String url = BASE_URL + "/auth/getUserDetailsByUserIds";
    ResponseEntity<List<GetUserDetailsResponse>> responseEntity =
        restClient
            .post()
            .uri(url)
            .headers(h -> h.addAll(AppUtils.getHttpHeader()))
            .body(request)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {});

    if (responseEntity.getBody() == null || HttpStatus.OK != responseEntity.getStatusCode()) {
      throw new ApplicationException(ExceptionConstants.HTTP_RESPONSE_NOT_VALID);
    }
    return responseEntity.getBody();
  }

  @Cacheable(
      value = CacheConstants.USER_DETAILS_CACHE_VALUE,
      key = "T(com.kadioglumf.websocket.constant.CacheConstants).ROLE_KEY + '_' + #role")
  public Set<GetUserDetailsResponse> getUserDetailsByRole(String role) {
    String url = BASE_URL + "/auth/getUserDetailsByRoles";
    GetUserDetailsByRolesRequest request = new GetUserDetailsByRolesRequest();
    request.setRoles(Set.of(role));

    ResponseEntity<Set<GetUserDetailsResponse>> responseEntity =
        restClient
            .post()
            .uri(url)
            .headers(h -> h.addAll(AppUtils.getHttpHeader()))
            .body(request)
            .retrieve()
            .toEntity(new ParameterizedTypeReference<>() {});

    if (responseEntity.getBody() == null || HttpStatus.OK != responseEntity.getStatusCode()) {
      throw new ApplicationException(ExceptionConstants.HTTP_RESPONSE_NOT_VALID);
    }
    return responseEntity.getBody();
  }

  @Cacheable(
      value = RedisCacheValueConstants.FIVE_MINUTE_CACHE_KEY,
      key = "{#root.methodName, #email}")
  public UserDto findByEmail(String email) {
    return restClient
        .get()
        .uri(
            BASE_URL,
            uriBuilder -> uriBuilder.path("/auth/findByEmail").queryParam("email", email).build())
        .retrieve()
        .toEntity(UserDto.class)
        .getBody();
  }
}
