package com.kadioglumf.authservice.security.oauth2;

import com.kadioglumf.authservice.enums.ClientTypeEnum;
import com.kadioglumf.authservice.security.CustomUserDetails;
import com.kadioglumf.authservice.security.TokenProvider;
import com.kadioglumf.authservice.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
@Log4j2
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final TokenProvider tokenProvider;
  private final RefreshTokenService refreshTokenService;

  @Value("${project.oauth2.redirectUri}")
  private String redirectUri;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    handle(request, response, authentication);
    super.clearAuthenticationAttributes(request);
  }

  @Override
  protected void handle(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String targetUrl =
        redirectUri.isEmpty() ? determineTargetUrl(request, response, authentication) : redirectUri;

    try {
      var userDetails = (CustomUserDetails) authentication.getPrincipal();
      String accessToken =
          tokenProvider.generateAccessToken((CustomUserDetails) authentication.getPrincipal());
      String refreshToken =
          refreshTokenService.createRefreshToken(userDetails.getId(), ClientTypeEnum.PUBLIC_CLIENT);
      targetUrl =
          UriComponentsBuilder.fromUriString(targetUrl)
              .queryParam("accessToken", accessToken)
              .queryParam("refreshToken", refreshToken)
              .build()
              .toUriString();
    } catch (Exception e) {
      log.error("OAuth2AuthenticationSuccessHandler.handle error: ", e);
      targetUrl =
          UriComponentsBuilder.fromUriString(redirectUri)
              .queryParam("errorCode", "OAUTH2_FAILED")
              .build()
              .toUriString();
    } finally {
      getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
  }
}
