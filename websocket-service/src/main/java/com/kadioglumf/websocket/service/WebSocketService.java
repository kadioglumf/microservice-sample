package com.kadioglumf.websocket.service;

import com.kadioglumf.websocket.payload.request.WebSocketRequest;
import com.kadioglumf.websocket.payload.request.WsSendMessageRequest;
import com.kadioglumf.websocket.socket.utils.SubscriptionHubUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
  private final NotificationService notificationService;

  public void sendNotification(WebSocketRequest request) {
    WsSendMessageRequest sendMessageRequest = new WsSendMessageRequest();
    sendMessageRequest.setChannel(request.getChannel());
    sendMessageRequest.setPayload(request.getMessage());
    sendMessageRequest.setCategory(request.getCategory());
    sendMessageRequest.setInfoType(request.getInfoType());
    sendMessageRequest.setSendingType(request.getSendingType());
    sendMessageRequest.setUserId(request.getUserId());
    sendMessageRequest.setRole(request.getRole());

    SubscriptionHubUtils.send(sendMessageRequest);
    notificationService.save(sendMessageRequest);
  }
}
