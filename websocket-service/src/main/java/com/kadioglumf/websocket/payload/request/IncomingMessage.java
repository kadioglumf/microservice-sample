package com.kadioglumf.websocket.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.websocket.core.enums.RoleTypeEnum;
import com.kadioglumf.websocket.enums.ActionType;
import com.kadioglumf.websocket.enums.WsCategoryType;
import com.kadioglumf.websocket.enums.WsInfoType;
import com.kadioglumf.websocket.enums.WsSendingType;
import com.kadioglumf.websocket.payload.*;
import com.kadioglumf.websocket.payload.WsCategoryTypeConverter;
import com.kadioglumf.websocket.payload.WsInfoTypeConverter;
import com.kadioglumf.websocket.socket.WebSocketRequestDispatcher;
import com.kadioglumf.websocket.socket.annotations.Action;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

/**
 * Incoming message received via WebSocket. The raw message is a JSON string in the following
 * format:
 *
 * <pre>
 * {
 *   "channel": required|String
 *   "action": required|String
 *   "payload": required|String
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class IncomingMessage {

  /**
   * Specify the channel for this message. {@link WebSocketRequestDispatcher} will route the request
   * to the corresponding channel.
   */
  private String channel;

  /**
   * Specify the action to take. {@link WebSocketRequestDispatcher} will find the corresponding
   * action method by checking the {@link Action} settings action must be in {@link ActionType}
   */
  private String action;

  /** The payload of the message that an action method will receive as its input. */
  private Object payload;

  /** for what purpose the message was sent */
  @Convert(converter = WsInfoTypeConverter.class)
  private WsInfoType infoType;

  /** The category type of message */
  @Convert(converter = WsCategoryTypeConverter.class)
  private WsCategoryType category;

  private WsSendingType sendingType;
  private RoleTypeEnum role;
  private Long userId;
}
