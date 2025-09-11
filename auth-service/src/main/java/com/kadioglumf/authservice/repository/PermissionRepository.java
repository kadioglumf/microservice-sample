package com.kadioglumf.authservice.repository;

import com.kadioglumf.authservice.models.PermissionModel;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionModel, Long> {
  Set<PermissionModel> findAllByPermissionIn(List<String> permissions);
}
