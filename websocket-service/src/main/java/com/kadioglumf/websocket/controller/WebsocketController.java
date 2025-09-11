package com.kadioglumf.websocket.controller;

import com.kadioglumf.websocket.constant.CacheConstants;
import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.secure.Secure;
import com.kadioglumf.websocket.socket.WebSocketRequestDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
@RequiredArgsConstructor
public class WebsocketController {

  private final WebSocketRequestDispatcher webSocketRequestDispatcher;

  @PostMapping("/scheduler/checkTokenOfAllSessions")
  @Secure(role = RoleTypeEnum.ROLE_SYSTEM)
  public ResponseEntity<Void> processRedisErrorData() {
    webSocketRequestDispatcher.checkTokenOfAllSessions();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/userDetails/cache/refresh")
  @Secure(role = RoleTypeEnum.ROLE_SYSTEM)
  @CacheEvict(value = CacheConstants.USER_DETAILS_CACHE_VALUE, allEntries = true)
  public ResponseEntity<Void> refresh() {
    return ResponseEntity.ok().build();
  }
}
