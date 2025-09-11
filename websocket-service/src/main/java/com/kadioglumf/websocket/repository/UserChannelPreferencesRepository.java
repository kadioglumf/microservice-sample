package com.kadioglumf.websocket.repository;

import com.kadioglumf.websocket.model.UserChannelPreferencesModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChannelPreferencesRepository
    extends JpaRepository<UserChannelPreferencesModel, Long> {
  List<UserChannelPreferencesModel> findAllByUserId(Long userId);

  List<UserChannelPreferencesModel> findAllByChannel_Name(String channelName);
}
