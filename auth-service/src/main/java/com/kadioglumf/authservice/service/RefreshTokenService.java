package com.kadioglumf.authservice.service;

import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.core.exception.BusinessException;
import com.kadioglumf.authservice.enums.ClientTypeEnum;
import com.kadioglumf.authservice.enums.IpTypeEnum;
import com.kadioglumf.authservice.models.RefreshTokenModel;
import com.kadioglumf.authservice.repository.CredentialsRepository;
import com.kadioglumf.authservice.repository.RefreshTokenRepository;
import com.kadioglumf.authservice.security.TokenProvider;
import com.kadioglumf.authservice.util.UserDeviceDetailsUtils;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;
  private final CredentialsRepository userRepository;

  public String createRefreshToken(Long userId, ClientTypeEnum clientType) {
    RefreshTokenModel token =
        refreshTokenRepository.findByCredentialIdAndCreatedByIpAddrAndClientType(
            userId, UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.CLIENT), clientType);
    if (token != null) {
      return token.getRefreshToken();
    }

    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));

    RefreshTokenModel refreshTokenModel =
        RefreshTokenModel.builder()
            .refreshToken(UUID.randomUUID().toString())
            .credential(user)
            .expiryDate(
                Instant.now().plus(tokenProvider.refreshTokenExpireDate(), ChronoUnit.SECONDS))
            .clientType(clientType)
            .build();
    refreshTokenRepository.save(refreshTokenModel);
    return refreshTokenModel.getRefreshToken();
  }

  public RefreshTokenModel findByRefreshTokenAndCheckExpiration(
      String refreshToken, ClientTypeEnum clientType) {
    var refreshTokenModel =
        refreshTokenRepository
            .findByCreatedByIpAddrAndClientTypeAndRefreshToken(
                UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.CLIENT), clientType, refreshToken)
            .orElseThrow(() -> new BusinessException(ExceptionConstants.REFRESH_TOKEN_NOT_FOUND));

    verifyExpiration(refreshTokenModel);

    if (clientType.getAllowedRoles().stream()
        .noneMatch(ar -> refreshTokenModel.getCredential().getRoles().contains(ar))) {
      throw new BusinessException(ExceptionConstants.AUTHENTICATION_CREDENTIALS_NOT_FOUND);
    }
    return refreshTokenModel;
  }

  public RefreshTokenModel findByUserIdAndCreatedByIpAddrAndClientType(
      Long userId, String ipAddress, ClientTypeEnum clientType) {
    return refreshTokenRepository.findByCredentialIdAndCreatedByIpAddrAndClientType(
        userId, ipAddress, clientType);
  }

  public List<RefreshTokenModel> findAllByCredentialId(Long userId) {
    return refreshTokenRepository.findAllByCredentialId(userId);
  }

  public void delete(RefreshTokenModel refreshTokenModel) {
    refreshTokenModel.setDeleted(true);
    refreshTokenRepository.save(refreshTokenModel);
  }

  private void verifyExpiration(RefreshTokenModel tokenModel) {
    if (tokenModel.getExpiryDate().compareTo(Instant.now()) < 0
        || !UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.CLIENT)
            .equals(tokenModel.getCreatedByIpAddr())) {
      tokenModel.setDeleted(true);
      refreshTokenRepository.save(tokenModel);
      refreshTokenRepository.flush();
      throw new BusinessException(ExceptionConstants.REFRESH_TOKEN_EXPIRED);
    }
  }
}
