package com.kadioglumf.websocket.service;

import com.kadioglumf.websocket.adapter.AuthServiceAdapter;
import com.kadioglumf.websocket.constant.CacheConstants;
import com.kadioglumf.websocket.model.ChannelModel;
import com.kadioglumf.websocket.model.UserChannelPreferencesModel;
import com.kadioglumf.websocket.payload.request.channel.AddChannelRequest;
import com.kadioglumf.websocket.payload.request.channel.DeleteChannelRequest;
import com.kadioglumf.websocket.payload.response.channel.ChannelResponse;
import com.kadioglumf.websocket.payload.response.channel.UserPreferencesResponse;
import com.kadioglumf.websocket.repository.ChannelRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class ChannelService {
  private final ChannelRepository channelRepository;
  private final AuthServiceAdapter authServiceAdapter;

  public List<ChannelResponse> getAll() {
    var channels = channelRepository.findAll();
    return channels.stream()
        .map(
            c ->
                ChannelResponse.builder()
                    .name(c.getName())
                    .roles(c.getRoles())
                    .userPreferences(
                        c.getUserChannelPreferences().stream()
                            .map(
                                p ->
                                    UserPreferencesResponse.builder()
                                        .userId(p.getUserId())
                                        .isSubscribed(p.isSubscribed())
                                        .channel(p.getChannel().getName())
                                        .build())
                            .collect(Collectors.toList()))
                    .build())
        .collect(Collectors.toList());
  }

  @Cacheable(
      value = CacheConstants.CHANNEL_CACHE_VALUE,
      key = "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #name")
  public ChannelResponse getByName(String name) {
    var channel = channelRepository.findByName(name);
    return channel
        .map(
            value ->
                ChannelResponse.builder()
                    .name(value.getName())
                    .roles(value.getRoles())
                    .userPreferences(
                        value.getUserChannelPreferences().stream()
                            .map(
                                p ->
                                    UserPreferencesResponse.builder()
                                        .userId(p.getUserId())
                                        .isSubscribed(p.isSubscribed())
                                        .channel(p.getChannel().getName())
                                        .build())
                            .collect(Collectors.toList()))
                    .build())
        .orElse(null);
  }

  @Transactional
  public void addChannel(AddChannelRequest request) {
    ChannelModel channel = new ChannelModel();
    channel.setName(request.getName());
    channel.setRoles(request.getRoles());
    channelRepository.save(channel);
  }

  @CacheEvict(
      value = CacheConstants.CHANNEL_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #request.name")
  @Transactional
  public void deleteChannel(DeleteChannelRequest request) {
    channelRepository.deleteByName(request.getName());
  }

  @Caching(
      evict = {
        @CacheEvict(
            value = CacheConstants.CHANNEL_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #channel"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' + #userId"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_NAME_KEY + '_' + #channel")
      })
  @Transactional
  public void subscribeChannel(String channel, Long userId) {
    channelRepository
        .findByName(channel)
        .ifPresent(
            channelModel -> {
              var users = authServiceAdapter.getUserDetailsByUserId(userId);
              if (!CollectionUtils.isEmpty(users)
                  && channelModel.getRoles().stream()
                      .anyMatch(r -> users.getFirst().getRoles().contains(r))) {
                if (channelModel.getUserChannelPreferences().stream()
                    .noneMatch(c -> c.getUserId().equals(userId))) {
                  var preference =
                      UserChannelPreferencesModel.builder()
                          .channel(channelModel)
                          .userId(userId)
                          .isSubscribed(true)
                          .build();
                  channelModel.getUserChannelPreferences().add(preference);
                  channelRepository.save(channelModel);
                } else if (channelModel.getUserChannelPreferences().stream()
                    .anyMatch(c -> c.getUserId().equals(userId) && !c.isSubscribed())) {
                  channelModel.getUserChannelPreferences().stream()
                      .filter(c -> c.getUserId().equals(userId))
                      .findFirst()
                      .ifPresent(p -> p.setSubscribed(true));
                  channelRepository.save(channelModel);
                }
              }
            });
  }

  @Caching(
      evict = {
        @CacheEvict(
            value = CacheConstants.CHANNEL_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_CACHE_KEY + '_' + #channel"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' + #userId"),
        @CacheEvict(
            value = CacheConstants.USER_CHANNEL_PREFERENCES_CACHE_VALUE,
            key =
                "T(com.kadioglumf.websocket.constant.CacheConstants).CHANNEL_NAME_KEY + '_' + #channel")
      })
  @Transactional
  public void unsubscribeChannel(String channel, Long userId) {
    channelRepository
        .findByName(channel)
        .ifPresent(
            channelModel -> {
              if (channelModel.getUserChannelPreferences().stream()
                  .anyMatch(c -> c.getUserId().equals(userId))) {
                channelModel.getUserChannelPreferences().stream()
                    .filter(c -> c.getUserId().equals(userId))
                    .findFirst()
                    .ifPresent(p -> p.setSubscribed(false));
                channelRepository.save(channelModel);
              }
            });
  }

  @Caching(evict = {@CacheEvict(value = CacheConstants.CHANNEL_CACHE_VALUE, allEntries = true)})
  public void refreshChannels() {}
}
