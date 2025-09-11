package com.kadioglumf.websocket.socket;

import com.kadioglumf.websocket.adapter.AuthServiceAdapter;
import com.kadioglumf.websocket.core.dto.UserDto;
import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.enums.ActionType;
import com.kadioglumf.websocket.enums.WsFailureType;
import com.kadioglumf.websocket.enums.WsReplyType;
import com.kadioglumf.websocket.payload.request.IncomingMessage;
import com.kadioglumf.websocket.socket.handler.ActionInvoker;
import com.kadioglumf.websocket.socket.utils.SubscriptionHubUtils;
import com.kadioglumf.websocket.util.ConvertUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Log4j2
public class WebSocketRequestDispatcher extends TextWebSocketHandler {

  private final ActionInvoker actionInvoker;
  private final JwtDecoder jwtDecoder;
  private final AuthServiceAdapter authServiceAdapter;

  private final Map<String, RealTimeSession> allSessions = new HashMap<>();

  public WebSocketRequestDispatcher(
      ActionInvoker actionInvoker, JwtDecoder jwtDecoder, AuthServiceAdapter authServiceAdapter) {
    this.actionInvoker = actionInvoker;
    this.jwtDecoder = jwtDecoder;
    this.authServiceAdapter = authServiceAdapter;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession webSocketSession) throws IOException {
    log.debug("WebSocket connection established");
    RealTimeSession session = new RealTimeSession(webSocketSession);
    try {
      var user = getUserDetails(session.getTokenFromUrl());
      session.setUserDetails(user);
      session.setLastValidToken(session.getTokenFromUrl());
      session.reply(WsReplyType.AUTHENTICATION_SUCCESS.getValue(), null);
      allSessions.put(session.id(), session);

      IncomingMessage incomingMessage = new IncomingMessage();
      incomingMessage.setAction(ActionType.SUBSCRIBE_ALL.getValue());
      handleTextMessage(
          webSocketSession, new TextMessage(ConvertUtils.convertObjectToJsonData(incomingMessage)));
    } catch (BusinessException exception) {
      log.debug("Authentication failed");
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    } catch (Exception exception) {
      log.debug("Error afterConnectionEstablished method: {}", exception.getMessage());
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      webSocketSession.close(CloseStatus.SERVER_ERROR);
    }
  }

  @Override
  protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message)
      throws IOException {
    RealTimeSession session = allSessions.get(webSocketSession.getId());
    log.debug("RealTimeSession[{}] Received message `{}`", session.id(), message.getPayload());

    IncomingMessage incomingMessage =
        ConvertUtils.convertJsonDataToObject(message.getPayload(), IncomingMessage.class);
    if (incomingMessage == null) {
      session.fail(WsFailureType.ILLEGAL_MESSAGE_FORMAT_FAILURE.getValue());
      return;
    }

    actionInvoker.invokeAction(incomingMessage, session, this::refreshConnection);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) {
    try {
      RealTimeSession session = allSessions.get(webSocketSession.getId());
      if (session != null) {
        SubscriptionHubUtils.unsubscribeAll(session);
        allSessions.remove(session.id());
        session.wrapped().close(status);
        log.debug(
            "RealTimeSession[{}] Unsubscribed all channels after disconnecting", session.id());
      }
    } catch (IOException e) {
      log.error("Error afterConnectionClosed method: {}", e.getMessage());
    }
  }

  private void refreshConnection(RealTimeSession session, String token) {
    try {
      var user = getUserDetails(token);
      session.setLastValidToken(token);
      session.reply(WsReplyType.AUTHENTICATION_REFRESH_SUCCESS.getValue(), null);
    } catch (BusinessException exception) {
      session.fail(WsFailureType.AUTHENTICATION_FAILURE.getValue());
      afterConnectionClosed(session.wrapped(), CloseStatus.SERVER_ERROR);
    } catch (Exception exception) {
      log.debug("Error handleTextMessage method: {}", exception.getMessage());
      session.fail(WsFailureType.UNKNOWN_FAILURE.getValue());
      afterConnectionClosed(session.wrapped(), CloseStatus.SERVER_ERROR);
    }
  }

  public void checkTokenOfAllSessions() {
    for (var session : allSessions.entrySet()) {
      if (session.getValue().isSubscriberTokenExpired()) {
        session.getValue().fail(WsFailureType.AUTH_TOKEN_EXPIRED_FAILURE.getValue());
        afterConnectionClosed(session.getValue().wrapped(), CloseStatus.SERVER_ERROR);
      }
    }
  }

  private UserDto getUserDetails(String token) {
    String email = jwtDecoder.decode(token).getClaimAsString("sub");
    return authServiceAdapter.findByEmail(email);
  }
}
