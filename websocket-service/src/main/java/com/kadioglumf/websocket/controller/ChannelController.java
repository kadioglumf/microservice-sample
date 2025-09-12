package com.kadioglumf.websocket.controller;

import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.core.secure.Secure;
import com.kadioglumf.websocket.payload.request.channel.AddChannelRequest;
import com.kadioglumf.websocket.payload.request.channel.DeleteChannelRequest;
import com.kadioglumf.websocket.payload.request.channel.SubscribeChannelRequest;
import com.kadioglumf.websocket.payload.request.channel.UnsubscribeChannelRequest;
import com.kadioglumf.websocket.payload.response.channel.ChannelResponse;
import com.kadioglumf.websocket.payload.response.channel.UserPreferencesResponse;
import com.kadioglumf.websocket.service.ChannelService;
import com.kadioglumf.websocket.service.UserChannelPreferencesService;
import com.kadioglumf.websocket.util.AuthUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Channel Documentation", description = "Channel Documentation")
@RestController
@RequestMapping("/websocket/channel")
@RequiredArgsConstructor
@Validated
public class ChannelController {
  private final ChannelService channelService;
  private final UserChannelPreferencesService userChannelPreferencesService;

  @PostMapping("/cache/refresh")
  @Operation(summary = "Refresh Channels")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Void> refresh() {
    channelService.refreshChannels();
    return ResponseEntity.ok().build();
  }

  @PostMapping("/add")
  @Operation(summary = "Add Channel")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Void> add(@Valid @RequestBody AddChannelRequest request) {
    channelService.addChannel(request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  @Operation(summary = "Delete Channel")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<Void> delete(@Valid @RequestBody DeleteChannelRequest request) {
    channelService.deleteChannel(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/getAll")
  @Operation(summary = "Get all Channels")
  @Secure(role = RoleTypeEnum.ROLE_ADMIN)
  public ResponseEntity<List<ChannelResponse>> getAll() {
    return ResponseEntity.ok(channelService.getAll());
  }

  @PostMapping("/subscribe")
  @Operation(summary = "Subscribe Channel")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN})
  public ResponseEntity<Void> subscribe(@Valid @RequestBody SubscribeChannelRequest request) {
    channelService.subscribeChannel(request.getName(), AuthUtils.getUserId());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/unsubscribe")
  @Operation(summary = "Unsubscribe Channel")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN})
  public ResponseEntity<Void> unsubscribe(@Valid @RequestBody UnsubscribeChannelRequest request) {
    channelService.unsubscribeChannel(request.getName(), AuthUtils.getUserId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/getUserChannels")
  @Operation(summary = "Get user Channels")
  @Secure(role = {RoleTypeEnum.ROLE_USER, RoleTypeEnum.ROLE_ADMIN})
  public ResponseEntity<List<UserPreferencesResponse>> getUserChannels() {
    return ResponseEntity.ok(userChannelPreferencesService.getUserChannels(AuthUtils.getUserId()));
  }
}
