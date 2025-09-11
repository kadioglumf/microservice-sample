package com.kadioglumf.websocket.validator;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.enums.WsFailureType;
import com.kadioglumf.websocket.payload.request.WsSendMessageRequest;
import com.kadioglumf.websocket.socket.RealTimeSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Log4j2
public class ActionHandlerValidator {
  private final ChannelValidator channelValidator;

  public void validSubscribe(String channel, RoleTypeEnum role) {
    channelValidator.valid(channel, role);
  }

  public void validUnSubscribe(String channel, RealTimeSession session) {
    if (StringUtils.isBlank(channel)) {
      throw new BusinessException(ExceptionConstants.WEB_SOCKET_ERROR);
    }
    if (session == null) {
      throw new BusinessException(ExceptionConstants.WEB_SOCKET_ERROR);
    }
    if (!session.wrapped().isOpen()) {
      log.error(
          "{} session closed. email: {} should reconnect.",
          session.id(),
          session.getUserDetails().getEmail());
      throw new BusinessException(ExceptionConstants.WEB_SOCKET_ERROR);
    }
  }

  public boolean isValidSend(RealTimeSession session, WsSendMessageRequest request) {
    channelValidator.valid(request.getChannel(), session.getUserDetails().getRole());
    if (request.getCategory() == null || request.getInfoType() == null) {
      session.fail(WsFailureType.MISSING_FIELD_FAILURE.getValue());
      return false;
    }
    if (!session.isAdmin()) {
      session.fail(WsFailureType.SEND_FAILURE.getValue());
      return false;
    }
    return true;
  }
}
