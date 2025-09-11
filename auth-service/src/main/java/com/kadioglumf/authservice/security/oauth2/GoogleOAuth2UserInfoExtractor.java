package com.kadioglumf.authservice.security.oauth2;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.enums.OAuth2ProviderEnum;
import com.kadioglumf.authservice.security.CustomUserDetails;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

  @Override
  public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
    CustomUserDetails customUserDetails = new CustomUserDetails();
    customUserDetails.setUsername(retrieveAttr("email", oAuth2User));
    customUserDetails.setName(retrieveAttr("name", oAuth2User));
    customUserDetails.setAvatarUrl(retrieveAttr("picture", oAuth2User));
    customUserDetails.setProvider(OAuth2ProviderEnum.GOOGLE);
    customUserDetails.setAttributes(oAuth2User.getAttributes());
    customUserDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(RoleTypeEnum.ROLE_USER.name())));
    return customUserDetails;
  }

  @Override
  public boolean accepts(OAuth2UserRequest userRequest) {
    return OAuth2ProviderEnum.GOOGLE
        .name()
        .equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
  }

  private String retrieveAttr(String attr, OAuth2User oAuth2User) {
    Object attribute = oAuth2User.getAttributes().get(attr);
    return attribute == null ? "" : attribute.toString();
  }
}
