package com.kadioglumf.authservice.service;

import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.constant.RedisCacheValueConstants;
import com.kadioglumf.authservice.core.exception.BusinessException;
import com.kadioglumf.authservice.models.PermissionModel;
import com.kadioglumf.authservice.payload.response.PermissionResponse;
import com.kadioglumf.authservice.repository.PermissionRepository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {
  private final PermissionRepository permissionRepository;
  private final AuthService authService;

  @Cacheable(value = RedisCacheValueConstants.GROUPED_PERMISSIONS)
  public List<PermissionResponse> getPermissions() {
    var permissions = permissionRepository.findAll();
    return getGroupedPermissions(Set.copyOf(permissions));
  }

  @Cacheable(value = RedisCacheValueConstants.USER_PERMISSIONS)
  public List<PermissionResponse> getUserPermissions(Long userId) {
    var user = authService.getUserById(userId);
    if (user.isEmpty()) {
      throw new BusinessException(ExceptionConstants.USER_NOT_FOUND);
    }
    return getGroupedPermissions(user.get().getPermissions());
  }

  private List<PermissionResponse> getGroupedPermissions(Set<PermissionModel> permissions) {
    Map<String, List<String>> groupedPermissions =
        permissions.stream()
            .collect(
                Collectors.groupingBy(
                    p -> p.getPermission().split("\\.")[0],
                    Collectors.mapping(PermissionModel::getPermission, Collectors.toList())));

    PermissionResponse response = new PermissionResponse();
    response.setPermission(groupedPermissions);

    return List.of(response);
  }

  public Set<PermissionModel> getPermissionsByList(List<String> request) {
    var permissions = permissionRepository.findAllByPermissionIn(request);
    if (request.size() != permissions.size()) {
      throw new BusinessException(ExceptionConstants.MISSING_PERMISSION_ERROR);
    }
    return permissions;
  }

  /*
   @Cacheable(value = RedisCacheValueConstants.GROUPED_PERMISSIONS)
   public PermissionTreeResponse getGroupedPermissions() {
     var permissions = permissionRepository.findAll();
     return getGroupedPermissions(Set.copyOf(permissions));
   }


   @Cacheable(value = RedisCacheValueConstants.USER_PERMISSIONS)
   public PermissionTreeResponse getUserPermissions(Long userId) {
     var user = authService.getUserById(userId);
     if (user.isEmpty()) {
       throw new BusinessException(ExceptionConstants.USER_NOT_FOUND);
     }
     return getGroupedPermissions(user.get().getPermissions());
   }

   private PermissionTreeResponse getGroupedPermissions(Set<PermissionModel> permissions) {
     Map<String, Map<String, List<String>>> grouped = new HashMap<>();

     for (PermissionModel permission : permissions) {
       String[] chains = permission.getPermission().split("\\.");

       if (chains[0] != null && chains[1] != null) {
         grouped
             .computeIfAbsent(chains[0], k -> new HashMap<>())
             .computeIfAbsent(chains[1], k -> new ArrayList<>());

         if (chains[2] != null) {
           grouped.get(chains[0]).get(chains[1]).add(chains[2]);
         }
       }
     }

     return new PermissionTreeResponse(grouped);
   }

  */
}
