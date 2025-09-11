package com.kadioglumf.dataservice.security;

import com.kadioglumf.dataservice.adapter.AuthServiceAdapter;
import com.kadioglumf.dataservice.constant.ServiceConstants;
import com.kadioglumf.dataservice.util.UserThreadContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final AuthServiceAdapter authServiceAdapter;
  private final JwtDecoder jwtDecoder;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      var token = getJwtFromRequest(request);
      if (token.isPresent()) {
        String email = jwtDecoder.decode(token.get()).getClaimAsString("sub");
        var user = authServiceAdapter.findByEmail(email);
        UserThreadContext.setUser(user);
        UserThreadContext.setJwt(token.get());
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication", e);
    }

    try {
      chain.doFilter(request, response);
    } finally {
      UserThreadContext.clear();
    }
  }

  private Optional<String> getJwtFromRequest(HttpServletRequest request) {
    String tokenHeader = request.getHeader(ServiceConstants.TOKEN_HEADER);
    if (StringUtils.hasText(tokenHeader)) {
      if (tokenHeader.startsWith(ServiceConstants.TOKEN_PREFIX)) {
        return Optional.of(tokenHeader.replace(ServiceConstants.TOKEN_PREFIX, ""));
      }
      return Optional.of(tokenHeader);
    }
    return Optional.empty();
  }
}
