package com.kadioglumf.authservice.service;

import com.kadioglumf.authservice.enums.*;
import com.kadioglumf.authservice.payload.request.*;
import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.constant.RedisCacheValueConstants;
import com.kadioglumf.authservice.core.dto.UserDto;
import com.kadioglumf.authservice.core.exception.BusinessException;
import com.kadioglumf.authservice.enums.ClientTypeEnum;
import com.kadioglumf.authservice.enums.IpTypeEnum;
import com.kadioglumf.authservice.mapper.CustomUserDetailsMapper;
import com.kadioglumf.authservice.mapper.UserServiceMapper;
import com.kadioglumf.authservice.models.CredentialsModel;
import com.kadioglumf.authservice.models.UserLoginActivityModel;
import com.kadioglumf.authservice.payload.request.GetUserDetailsByRolesRequest;
import com.kadioglumf.authservice.payload.request.GetUserDetailsRequest;
import com.kadioglumf.authservice.payload.request.LoginRequest;
import com.kadioglumf.authservice.payload.response.CreateAccessTokenResponse;
import com.kadioglumf.authservice.payload.response.GetUserDetailsResponse;
import com.kadioglumf.authservice.payload.response.LoginResponse;
import com.kadioglumf.authservice.repository.CredentialsRepository;
import com.kadioglumf.authservice.repository.UserLoginActivityRepository;
import com.kadioglumf.authservice.security.CustomUserDetails;
import com.kadioglumf.authservice.security.TokenProvider;
import com.kadioglumf.authservice.util.AuthUtils;
import com.kadioglumf.authservice.util.UserDeviceDetailsUtils;
import com.kadioglumf.authservice.util.UserThreadContext;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AuthService {

  // Repository
  private final CredentialsRepository credentialsRepository;
  private final UserLoginActivityRepository userLoginActivityRepository;

  // Security
  @Lazy private final AuthenticationManager authenticationManager;
  private final TokenProvider tokenProvider;

  // Service
  private final RefreshTokenService refreshTokenService;

  // Mapper
  private final CustomUserDetailsMapper customUserDetailsMapper;
  private final UserServiceMapper userServiceMapper;

  public Optional<CredentialsModel> getUserByEmail(String email) {
    return credentialsRepository.findByEmail(email);
  }

  public Optional<CredentialsModel> getUserById(Long userId) {
    return credentialsRepository.findById(userId);
  }

  public CredentialsModel saveUser(CredentialsModel credential) {
    return credentialsRepository.save(credential);
  }

  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authentication =
        createAuthentication(loginRequest.getEmail(), loginRequest.getPassword());

    var userDetails = (CustomUserDetails) authentication.getPrincipal();

    String accessToken = tokenProvider.generateAccessToken(userDetails);

    var refreshToken =
        refreshTokenService.createRefreshToken(
            userDetails.getId(), ClientTypeEnum.fromOrigin(UserDeviceDetailsUtils.getOrigin()));

    userLoginActivityRepository.save(
        UserLoginActivityModel.builder().userId(userDetails.getId()).build());
    return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
  }

  @Cacheable(
      value = RedisCacheValueConstants.LOGOUT_JWT_CACHE_KEY,
      key = "{T(com.kadioglumf.authservice.util.UserThreadContext).getJwt()}")
  public void logout() {
    var origin = UserDeviceDetailsUtils.getOrigin();
    var refreshTokenModel =
        refreshTokenService.findByUserIdAndCreatedByIpAddrAndClientType(
            UserThreadContext.getUser().getId(),
            UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.CLIENT),
            ClientTypeEnum.fromOrigin(origin));
    if (refreshTokenModel != null) {
      refreshTokenService.delete(refreshTokenModel);
    } else {
      var refreshTokenModels =
          refreshTokenService.findAllByCredentialId(UserThreadContext.getUser().getId());
      refreshTokenModels.forEach(refreshTokenService::delete);
    }
  }

  public CreateAccessTokenResponse getAccessTokenUsingRefreshToken(String refreshToken) {
    var clientType = ClientTypeEnum.fromOrigin(UserDeviceDetailsUtils.getOrigin());
    var refreshTokenModel =
        refreshTokenService.findByRefreshTokenAndCheckExpiration(refreshToken, clientType);

    CredentialsModel credential = refreshTokenModel.getCredential();
    if (credential == null || !credential.getId().equals(AuthUtils.getUserId())) {
      throw new BusinessException(ExceptionConstants.REFRESH_TOKEN_NOT_FOUND);
    }

    String accessToken =
        tokenProvider.generateAccessToken(customUserDetailsMapper.toCustomUserDetails(credential));
    return CreateAccessTokenResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  public UserDto getUserDtoByEmail(String email) {
    var user = getUserByEmail(email);
    if (user.isEmpty()) {
      throw new BusinessException(ExceptionConstants.USER_NOT_FOUND);
    }
    return userServiceMapper.toUserDto(user.get());
  }

  public UserDto getUserDtoById(Long id) {
    var user = getUserById(id);
    if (user.isEmpty()) {
      throw new BusinessException(ExceptionConstants.USER_NOT_FOUND);
    }
    return userServiceMapper.toUserDto(user.get());
  }

  public long countUsers() {
    return credentialsRepository.count();
  }

  public Set<GetUserDetailsResponse> getUserDetailsByUserIds(GetUserDetailsRequest request) {
    request.getUserIds().remove(null);
    Set<CredentialsModel> users = credentialsRepository.findAllByIdIn(request.getUserIds());
    if (CollectionUtils.isEmpty(users) || users.size() != request.getUserIds().size()) {
      throw new BusinessException(ExceptionConstants.USER_NOT_FOUND);
    }
    return userServiceMapper.convertToResponse(users);
  }

  public Set<GetUserDetailsResponse> getUserDetailsByRoles(GetUserDetailsByRolesRequest request) {
    Set<CredentialsModel> users = credentialsRepository.findAllByRolesIn(request.getRoles());
    if (CollectionUtils.isEmpty(users)) {
      return new HashSet<>();
    }
    return userServiceMapper.convertToResponse(users);
  }

  private Authentication createAuthentication(String username, String password) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(username, password));
  }
}
