package com.kadioglumf.authservice.security;

import com.kadioglumf.authservice.core.dto.PermissionDto;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.enums.OAuth2ProviderEnum;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class CustomUserDetails implements OAuth2User, UserDetails {
  private Long id;
  private String username;
  private String password;
  private String name;
  private String email;
  private String avatarUrl;
  private OAuth2ProviderEnum provider;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;
  private Set<RoleTypeEnum> roles;
  private List<PermissionDto> permissions;
}
