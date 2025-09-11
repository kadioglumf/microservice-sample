package com.kadioglumf.authservice.security.oauth2;

import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.enums.OAuth2ProviderEnum;
import com.kadioglumf.authservice.enums.UserActivityEnum;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.models.UserModel;
import com.kadioglumf.authservice.security.CustomUserDetails;
import com.kadioglumf.authservice.service.AuthService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final AuthService authService;
  private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional =
        oAuth2UserInfoExtractors.stream()
            .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
            .findFirst();
    if (oAuth2UserInfoExtractorOptional.isEmpty()) {
      throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
    }

    CustomUserDetails customUserDetails =
        oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
    CredentialsModel user = upsertUser(customUserDetails);
    customUserDetails.setId(user.getId());
    return customUserDetails;
  }

  private CredentialsModel upsertUser(CustomUserDetails customUserDetails) {
    Optional<CredentialsModel> userOptional =
        authService.getUserByEmail(customUserDetails.getUsername());
    if (userOptional.isEmpty()) {
      CredentialsModel credential = new CredentialsModel();
      credential.setEmail(customUserDetails.getUsername());
      credential.setName(customUserDetails.getName());
      credential.setProvider(customUserDetails.getProvider());
      credential.setRoles(new HashSet<>(List.of(RoleTypeEnum.ROLE_USER)));
      credential.setActivity(UserActivityEnum.ACTIVE);

      UserModel userModel = new UserModel();
      userModel.setCredential(credential);

      credential.setUser(userModel);
      return authService.saveUser(credential);
    } else {
      var user = userOptional.get();
      if (!OAuth2ProviderEnum.valueOf(customUserDetails.getProvider().name())
          .equals(user.getProvider())) {
        throw new OAuth2AuthenticationException(ExceptionConstants.USER_LOGIN_PROVIDER_MISMATCH);
      }
    }
    return userOptional.get();
  }
}
