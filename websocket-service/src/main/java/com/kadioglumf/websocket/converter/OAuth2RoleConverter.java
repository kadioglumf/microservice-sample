package com.kadioglumf.websocket.converter;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class OAuth2RoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    ArrayList<String> roles = (ArrayList<String>) source.getClaims().get("roles");
    Collection<GrantedAuthority> returnValue = new ArrayList<>();
    if (roles != null && !roles.isEmpty()) {
      returnValue.addAll(roles.stream().map(SimpleGrantedAuthority::new).toList());
    }
    return returnValue;
  }
}
