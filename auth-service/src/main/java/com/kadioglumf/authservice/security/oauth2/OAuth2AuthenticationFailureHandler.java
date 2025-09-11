package com.kadioglumf.authservice.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Log4j2
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  @Value("${project.oauth2.redirectUri}")
  private String redirectUri;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    String targetUrl =
        UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("errorCode", "OAUTH2_FAILED")
            .build()
            .toUriString();

    log.error("Authentication failed. Error: ", exception);
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
