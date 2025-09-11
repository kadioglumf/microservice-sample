package com.kadioglumf.authservice.security;

import com.kadioglumf.authservice.constant.ServiceConstants;
import com.kadioglumf.authservice.core.dto.UserDto;
import com.kadioglumf.authservice.util.UserThreadContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
  private final UserDetailsService userDetailsService;
  private final TokenProvider tokenProvider;

  // private final StringRedisTemplate redisTemplate;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      var token = getJwtFromRequest(request);
      if (token.isPresent()) {
        /*        if (isKeyExists(token.get())) {
          throw new SecureException(ExceptionConstants.UNAUTHORIZED);
        }*/
        var username = tokenProvider.getClaim(token.get(), JwtClaimNames.SUB);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserThreadContext.setUser(enrichUserDto((CustomUserDetails) userDetails));
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

  private UserDto enrichUserDto(CustomUserDetails userDetails) {
    return UserDto.builder()
        .id(userDetails.getId())
        .name(userDetails.getName())
        .email(userDetails.getUsername())
        .roles(userDetails.getRoles())
        .permissions(userDetails.getPermissions())
        .build();
  }

  // Gateway de yapılıyor. o yüzden gerekli değil
  /*  public boolean isKeyExists(String jwt) {
    Boolean hasKey =
        redisTemplate.hasKey(RedisCacheValueConstants.LOGOUT_JWT_CACHE_KEY + "::" + jwt);
    return hasKey != null && hasKey;
  }*/
}
