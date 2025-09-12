package com.kadioglumf.websocket.repository;

import com.kadioglumf.websocket.model.ChannelModel;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<ChannelModel, Long> {
  void deleteByName(String name);

  Optional<ChannelModel> findByName(String name);

  @Query(" SELECT DISTINCT c FROM ChannelModel c WHERE EXISTS ( SELECT r FROM c.roles r WHERE r IN :roles) ")
  List<ChannelModel> findAllByRoles(@Param("roles") Set<String> roles);
}
