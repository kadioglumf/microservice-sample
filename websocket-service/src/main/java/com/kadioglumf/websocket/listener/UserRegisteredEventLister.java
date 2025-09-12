package com.kadioglumf.websocket.listener;

import com.kadioglumf.websocket.event.UserRegisteredEvent;
import com.kadioglumf.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class UserRegisteredEventLister extends BaseListener<UserRegisteredEvent> {

  private final WebSocketService webSocketService;

  @EventListener
  @Override
  public void handle(UserRegisteredEvent event) {
    log.info("UserRegisteredEventLister: {}", event);
    webSocketService.sendNotification(event);
  }
}
