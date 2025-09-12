package com.kadioglumf.websocket.service;

import com.kadioglumf.websocket.event.BaseWebsocketEvent;
import com.kadioglumf.websocket.payload.request.WsSendMessageRequest;
import com.kadioglumf.websocket.socket.utils.SubscriptionHubUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {
  private final NotificationService notificationService;

  public void sendNotification(BaseWebsocketEvent event) {
    WsSendMessageRequest sendMessageRequest = new WsSendMessageRequest();
    sendMessageRequest.setChannel(event.getChannel());
    sendMessageRequest.setPayload(event.getMessage());
    sendMessageRequest.setCategory(event.getCategory());
    sendMessageRequest.setInfoType(event.getInfoType());
    sendMessageRequest.setSendingType(event.getSendingType());
    sendMessageRequest.setUserId(event.getUserId());
    sendMessageRequest.setRole(event.getRole());

    SubscriptionHubUtils.send(sendMessageRequest);
    notificationService.save(sendMessageRequest);
  }
}
