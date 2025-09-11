package com.kadioglumf.authservice.mapper;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.security.CustomUserDetails;
import java.util.Collection;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Mapper(componentModel = "spring")
public interface CustomUserDetailsMapper {

  @Mapping(target = "authorities", source = "roles", qualifiedByName = "getAuthorities")
  @Mapping(target = "username", source = "email")
  @Mapping(target = "roles", source = "roles")
  CustomUserDetails toCustomUserDetails(CredentialsModel credential);

  @Named("getAuthorities")
  default Collection<? extends GrantedAuthority> getAuthorities(Set<RoleTypeEnum> roles) {
    return roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).toList();
  }
}
