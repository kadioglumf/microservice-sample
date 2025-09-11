package com.kadioglumf.authservice.service;

import com.kadioglumf.authservice.enums.*;
import com.kadioglumf.authservice.payload.request.*;
import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.core.exception.BusinessException;
import com.kadioglumf.authservice.enums.OAuth2ProviderEnum;
import com.kadioglumf.authservice.enums.UserActivityEnum;
import com.kadioglumf.authservice.mapper.UserServiceMapper;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.models.UserModel;
import com.kadioglumf.authservice.payload.request.SignupRequest;
import com.kadioglumf.authservice.payload.response.UserDetailsResponse;
import com.kadioglumf.authservice.repository.CredentialsRepository;
import com.kadioglumf.authservice.util.UserThreadContext;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  // Repository
  private final CredentialsRepository credentialsRepository;

  // Security
  @Lazy private final PasswordEncoder encoder;

  // Mapper
  private final UserServiceMapper userServiceMapper;

  public void registerUser(SignupRequest request) {
    var userModel = credentialsRepository.findByEmail(request.getEmail());
    if (userModel.isPresent()) {
      throw new BusinessException(ExceptionConstants.REGISTER_EMAIL_NOT_AVAILABLE_ERROR);
    }
    var credentialsModel =
        CredentialsModel.builder()
            .name(request.getName())
            .surname(request.getSurname())
            .email(request.getEmail())
            .password(encoder.encode(request.getPassword()))
            .mobile(request.getMobile())
            .nationality(request.getNationality())
            .gender(request.getGender())
            .activity(UserActivityEnum.PASSIVE)
            .provider(OAuth2ProviderEnum.LOCAL)
            .roles(new HashSet<>(List.of(RoleTypeEnum.ROLE_USER)))
            .build();

    var user =
        UserModel.builder()
            .birthDay(request.getBirthDay())
            .address(userServiceMapper.toUserAddressModel(request.getAddress()))
            .credential(credentialsModel)
            .build();

    credentialsModel.setUser(user);
    credentialsRepository.save(credentialsModel);

    // websocketServiceAdapter.userDetailsCacheRefresh();
    // sendEmailActivateUser(request);
  }

  public UserDetailsResponse getUserDetails() {
    CredentialsModel credentialsModel =
        credentialsRepository
            .findById(UserThreadContext.getUser().getId())
            .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
    return userServiceMapper.toUserDetailsResponse(credentialsModel);
  }
}
