package com.kadioglumf.authservice.repository;

import com.kadioglumf.authservice.enums.ClientTypeEnum;
import com.kadioglumf.authservice.models.RefreshTokenModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long> {
  RefreshTokenModel findByCredentialIdAndCreatedByIpAddrAndClientType(
      Long userId, String ipAddress, ClientTypeEnum clientType);

  Optional<RefreshTokenModel> findByCreatedByIpAddrAndClientTypeAndRefreshToken(
      String ipAddress, ClientTypeEnum clientType, String refreshToken);

  List<RefreshTokenModel> findAllByCredentialId(Long credentialId);
}
