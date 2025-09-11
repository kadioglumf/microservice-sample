package com.kadioglumf.websocket.validator;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.payload.response.channel.ChannelResponse;
import com.kadioglumf.websocket.service.ChannelService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ChannelValidator {
  private final ChannelService channelService;

  public void valid(String channel, RoleTypeEnum role) {
    ChannelResponse channelInfo = channelService.getByName(channel);
    if (channelInfo == null) {
      throw new BusinessException(ExceptionConstants.WEB_SOCKET_ERROR);
    }
    if (!isRolesAllowed(role, channelInfo.getRoles())) {
      throw new BusinessException(ExceptionConstants.WEB_SOCKET_ERROR);
    }
  }

  public boolean isRolesAllowed(RoleTypeEnum role, Set<String> allowedRoles) {
    if (CollectionUtils.isEmpty(allowedRoles)) {
      return true;
    }
    for (String allowedRole : allowedRoles) {
      if (role.name().equals(allowedRole)) {
        return true;
      }
    }
    return false;
  }
}
