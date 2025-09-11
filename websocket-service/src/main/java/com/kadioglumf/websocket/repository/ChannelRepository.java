package com.kadioglumf.websocket.repository;

import com.kadioglumf.websocket.model.ChannelModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<ChannelModel, Long> {
  void deleteByName(String name);

  Optional<ChannelModel> findByName(String name);

  @Query("SELECT distinct c FROM ChannelModel c JOIN c.roles r WHERE r IN :role")
  List<ChannelModel> findAllByRoles(@Param("role") String role);
}
