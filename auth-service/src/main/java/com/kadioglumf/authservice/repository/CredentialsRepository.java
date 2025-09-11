package com.kadioglumf.authservice.repository;

import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.models.CredentialsModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CredentialsRepository
    extends JpaRepository<CredentialsModel, Long>, JpaSpecificationExecutor<CredentialsModel> {
  Optional<CredentialsModel> findByEmail(String email);

  Set<CredentialsModel> findAllByIdIn(Set<Long> userIds);

  Set<CredentialsModel> findAllByRolesIn(Set<RoleTypeEnum> roles);

  Boolean existsByEmail(String email);

  Boolean existsByMobile(String email);

  Optional<CredentialsModel> findByIdAndRolesContaining(Long id, RoleTypeEnum role);

  @Query(
      "SELECT c FROM CredentialsModel c JOIN c.permissions p WHERE p.permission LIKE CONCAT(:prefix, '%')")
  List<CredentialsModel> findByPermissionPrefix(@Param("prefix") String prefix);
}
