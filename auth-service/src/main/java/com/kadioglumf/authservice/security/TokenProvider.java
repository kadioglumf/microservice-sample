package com.kadioglumf.authservice.security;

import com.kadioglumf.authservice.core.dto.BaseDto;
import com.kadioglumf.authservice.util.ConvertUtils;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

  @Lazy private final JwtEncoder jwtEncoder;
  @Lazy private final JwtDecoder jwtDecoder;

  @Value("${project.accessToken.expiration.seconds}")
  private Long jwtExpirationSeconds;

  @Value("${project.refreshToken.expiration.seconds}")
  private Long refreshTokenExpirationSeconds;

  public static final String AUTH_CLAIM_KEY = "auth-service";

  public String getClaim(String token, String claim) {
    Jwt jwt = jwtDecoder.decode(token);
    return jwt.getClaimAsString(claim);
  }

  public String generateAccessToken(CustomUserDetails userPrincipal) {
    List<String> roles =
        userPrincipal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    Consumer<Map<String, Object>> claimsConsumer =
        claimsMap -> {
          claimsMap.put(JwtClaimNames.SUB, userPrincipal.getUsername());
          claimsMap.put(JwtClaimNames.ISS, "https://www.microservice.sample.com");
          claimsMap.put(
              JwtClaimNames.EXP, Instant.now().plus(jwtExpirationSeconds, ChronoUnit.SECONDS));
          claimsMap.put(JwtClaimNames.IAT, Instant.now());
          claimsMap.put("roles", roles);
        };

    var jwt =
        jwtEncoder.encode(
            JwtEncoderParameters.from(JwtClaimsSet.builder().claims(claimsConsumer).build()));
    return jwt.getTokenValue();
  }

  public Jwt generateJwtToken(BaseDto request, String subject, int expirationMs) {
    var json = ConvertUtils.convertObjectToJsonData(request);
    byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
    String base64 = Base64.getEncoder().encodeToString(bytes);

    Consumer<Map<String, Object>> claimsConsumer =
        claimsMap -> {
          claimsMap.put(JwtClaimNames.SUB, subject);
          claimsMap.put(JwtClaimNames.ISS, "https://www.microservice.sample.com");
          claimsMap.put(JwtClaimNames.EXP, Instant.now().plus(expirationMs, ChronoUnit.SECONDS));
          claimsMap.put(JwtClaimNames.IAT, Instant.now());
          claimsMap.put(AUTH_CLAIM_KEY, base64);
        };

    return jwtEncoder.encode(
        JwtEncoderParameters.from(JwtClaimsSet.builder().claims(claimsConsumer).build()));
  }

  public long refreshTokenExpireDate() {
    return refreshTokenExpirationSeconds;
  }
}
