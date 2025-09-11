package com.kadioglumf.authservice.security;

import com.kadioglumf.authservice.mapper.CustomUserDetailsMapper;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final AuthService authService;
  private final CustomUserDetailsMapper customUserDetailsMapper;

  @Override
  public UserDetails loadUserByUsername(String email) {
    CredentialsModel user =
        authService
            .getUserByEmail(email)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("Email %s not found", email)));

    return customUserDetailsMapper.toCustomUserDetails(user);
  }
}
