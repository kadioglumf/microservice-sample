package com.kadioglumf.websocket.service;

import com.kadioglumf.websocket.adapter.AuthServiceAdapter;
import com.kadioglumf.websocket.constant.CacheConstants;
import com.kadioglumf.websocket.model.UserChannelPreferencesModel;
import com.kadioglumf.websocket.payload.response.channel.UserPreferencesResponse;
import com.kadioglumf.websocket.repository.ChannelRepository;
import com.kadioglumf.websocket.repository.UserChannelPreferencesRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class UserChannelPreferencesService {
  private final UserChannelPreferencesRepository userChannelPreferencesRepository;
  private final ChannelRepository channelRepository;
  private final AuthServiceAdapter authServiceAdapter;

  @Cacheable(
      value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
      key = "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' + #userId")
  @Transactional
  public List<UserPreferencesResponse> getUserChannels(Long userId) {
    var preferences = userChannelPreferencesRepository.findAllByUserId(userId);
    if (CollectionUtils.isEmpty(preferences)) {
      var users = authServiceAdapter.getUserDetailsByUserId(userId);
      var channels = channelRepository.findAllByRoles(users.getFirst().getRoles());
      channels.forEach(
          c -> {
            var preference =
                UserChannelPreferencesModel.builder()
                    .channel(c)
                    .userId(userId)
                    .isSubscribed(true)
                    .build();
            userChannelPreferencesRepository.save(preference);

            c.getUserChannelPreferences().add(preference);
            channelRepository.save(c);
          });

      return channels.stream()
          .map(
              c ->
                  UserPreferencesResponse.builder()
                      .channel(c.getName())
                      .isSubscribed(true)
                      .userId(userId)
                      .build())
          .collect(Collectors.toList());
    }
    return preferences.stream()
        .map(
            c ->
                UserPreferencesResponse.builder()
                    .channel(c.getChannel().getName())
                    .isSubscribed(c.isSubscribed())
                    .userId(userId)
                    .build())
        .collect(Collectors.toList());
  }

  @Cacheable(
      value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_NAME_KEY + '_' + #channelName")
  public List<UserPreferencesResponse> getByChannelName(String channelName) {
    var preferences = userChannelPreferencesRepository.findAllByChannel_Name(channelName);
    return preferences.stream()
        .map(
            c ->
                UserPreferencesResponse.builder()
                    .channel(c.getChannel().getName())
                    .isSubscribed(c.isSubscribed())
                    .userId(c.getUserId())
                    .build())
        .collect(Collectors.toList());
  }
}
