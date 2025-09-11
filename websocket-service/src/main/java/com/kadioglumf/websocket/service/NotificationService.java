package com.kadioglumf.websocket.service;

import com.kadioglumf.websocket.adapter.AuthServiceAdapter;
import com.kadioglumf.websocket.constant.CacheConstants;
import com.kadioglumf.websocket.enums.WsSendingType;
import com.kadioglumf.websocket.mapper.NotificationMapper;
import com.kadioglumf.websocket.model.NotificationModel;
import com.kadioglumf.websocket.payload.request.WsSendMessageRequest;
import com.kadioglumf.websocket.payload.response.channel.UserPreferencesResponse;
import com.kadioglumf.websocket.payload.response.notification.NotificationResponse;
import com.kadioglumf.websocket.repository.NotificationRepository;
import com.kadioglumf.websocket.util.AuthUtils;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
  private final AuthServiceAdapter authServiceAdapter;
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final CacheManager cacheManager;
  private final UserChannelPreferencesService userChannelPreferencesService;

  @Transactional
  public void save(WsSendMessageRequest request) {
    if (WsSendingType.SPECIFIED_USER.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());
      var usersIds =
          preferences.stream()
              .filter(p -> p.getUserId().equals(request.getUserId()))
              .filter(UserPreferencesResponse::isSubscribed)
              .map(UserPreferencesResponse::getUserId)
              .collect(Collectors.toSet());
      save(request, usersIds);
    } else if (WsSendingType.ROLE_BASED.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());
      if (!CollectionUtils.isEmpty(preferences)) {
        var users = authServiceAdapter.getUserDetailsByRole(request.getRole().name());
        var usersIds =
            preferences.stream()
                .filter(UserPreferencesResponse::isSubscribed)
                .map(UserPreferencesResponse::getUserId)
                .filter(userId -> users.stream().anyMatch(u -> u.getUserId().equals(userId)))
                .collect(Collectors.toSet());
        save(request, usersIds);
      }

    } else if (request.getSendingType() == null
        || WsSendingType.ALL.equals(request.getSendingType())) {
      var preferences = userChannelPreferencesService.getByChannelName(request.getChannel());

      var usersIds =
          preferences.stream()
              .filter(UserPreferencesResponse::isSubscribed)
              .map(UserPreferencesResponse::getUserId)
              .collect(Collectors.toSet());
      save(request, usersIds);
    }
  }

  public void save(WsSendMessageRequest request, Set<Long> userIds) {
    for (var userId : userIds) {
      var model = new NotificationModel();
      model.setNotificationId(UUID.randomUUID().toString());
      model.setMessage(request.getPayload().toString());
      model.setInfoType(request.getInfoType());
      model.setCategory(request.getCategory());
      model.setChannel(request.getChannel());
      model.setUserId(userId);
      notificationRepository.save(model);

      var cache = cacheManager.getCache(CacheConstants.NOTIFICATION_CACHE_VALUE);
      if (cache != null) {
        cache.evictIfPresent(CacheConstants.USER_ID_KEY + "_" + userId);
      }
    }
  }

  @Cacheable(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' "
              + "+ T(com.kadioglumf.websocket.auth.AuthService).getUserId()")
  public List<NotificationResponse> fetch() {
    var notifications =
        notificationRepository.findByUserIdOrderByCreationDateDesc(AuthUtils.getUserId());
    return notificationMapper.toNotificationResponseList(notifications);
  }

  @CacheEvict(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' "
              + "+ T(com.kadioglumf.websocket.auth.AuthService).getUserId()")
  @Transactional
  public void markAsRead(List<String> notificationId) {
    var notification = notificationRepository.findByNotificationIdIn(notificationId);
    if (notification == null) {
      return;
    }
    notification.forEach(n -> n.setRead(true));
    notificationRepository.saveAll(notification);
  }

  @CacheEvict(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' "
              + "+ T(com.kadioglumf.websocket.auth.AuthService).getUserId()")
  @Transactional
  public void deleteAll() {
    var notifications = notificationRepository.findByUserId(AuthUtils.getUserId());
    notifications.forEach(notification -> notification.setDeleted(true));
    notificationRepository.saveAll(notifications);
  }

  @CacheEvict(
      value = CacheConstants.NOTIFICATION_CACHE_VALUE,
      key =
          "T(com.kadioglumf.websocket.constant.CacheConstants).USER_ID_KEY + '_' "
              + "+ T(com.kadioglumf.websocket.auth.AuthService).getUserId()")
  @Transactional
  public void deleteByNotificationIds(List<String> notificationIds) {
    var notifications = notificationRepository.findByNotificationIdIn(notificationIds);
    if (CollectionUtils.isEmpty(notifications)) {
      return;
    }
    notifications.forEach(n -> n.setDeleted(true));
    notificationRepository.saveAll(notifications);
  }

  @Caching(
      evict = {@CacheEvict(value = CacheConstants.NOTIFICATION_CACHE_VALUE, allEntries = true)})
  public void refreshNotifications() {}
}
