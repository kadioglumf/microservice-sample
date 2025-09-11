package com.kadioglumf.websocket.socket.handler;

import com.kadioglumf.websocket.core.exception.BusinessException;
import com.kadioglumf.websocket.enums.ActionType;
import com.kadioglumf.websocket.enums.WsFailureType;
import com.kadioglumf.websocket.payload.request.IncomingMessage;
import com.kadioglumf.websocket.payload.request.WsSendMessageRequest;
import com.kadioglumf.websocket.socket.RealTimeSession;
import com.kadioglumf.websocket.socket.annotations.Action;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Log4j2
public class ActionInvoker {
  private final ActionHandler actionHandler;
  private final Map<ActionType, Method> actionMethods = new HashMap<>();

  public ActionInvoker(ActionHandler actionHandler) {
    this.actionHandler = actionHandler;
    bootstrap();
  }

  private void bootstrap() {
    Method[] methods = actionHandler.getClass().getMethods();
    for (Method method : methods) {
      Action actionAnnotation = method.getAnnotation(Action.class);
      if (actionAnnotation == null) {
        continue;
      }

      ActionType action = actionAnnotation.value();
      actionMethods.put(action, method);
    }
  }

  public void invokeAction(
      IncomingMessage incomingMessage,
      RealTimeSession session,
      BiConsumer<RealTimeSession, String> func) {
    try {
      if (!supports(incomingMessage.getAction())) {
        String errorMessage =
            "Action not found `"
                + incomingMessage.getAction()
                + "` at channel `"
                + incomingMessage.getChannel()
                + "`";
        session.fail(WsFailureType.ACTION_TYPE_FAILURE.getValue());
        log.error("RealTimeSession[{}] {}", session.id(), errorMessage);
        return;
      }

      Method actionMethod = actionMethods.get(ActionType.getFromValue(incomingMessage.getAction()));
      Assert.notNull(
          actionMethod, "Action method for `" + incomingMessage.getAction() + "` must exist");

      // Find all required parameters
      Class<?>[] parameterTypes = actionMethod.getParameterTypes();

      // The arguments that will be passed to the action method
      Object[] args = new Object[parameterTypes.length];

      for (int i = 0; i < parameterTypes.length; i++) {
        Class<?> parameterType = parameterTypes[i];

        if (parameterType.isInstance(session)) {
          args[i] = session;
        } else if (parameterType.isAssignableFrom(WsSendMessageRequest.class)) {
          var sendMessageRequest =
              WsSendMessageRequest.builder()
                  .infoType(incomingMessage.getInfoType())
                  .category(incomingMessage.getCategory())
                  .payload(incomingMessage.getPayload())
                  .channel(incomingMessage.getChannel())
                  .sendingType(incomingMessage.getSendingType())
                  .role(incomingMessage.getRole())
                  .userId(incomingMessage.getUserId())
                  .build();

          args[i] = sendMessageRequest;
        } else if (parameterType.isAssignableFrom(BiConsumer.class)) {
          args[i] = func;
        }
      }

      actionMethod.invoke(actionHandler, args);
    } catch (Exception e) {
      if (e instanceof InvocationTargetException
          && ((InvocationTargetException) e).getTargetException() instanceof BusinessException) {
        BusinessException exception =
            (BusinessException) ((InvocationTargetException) e).getTargetException();
        String error = exception.getExceptionCode() + ": " + exception.getExceptionMessage();
        log.error(error, e);
        session.fail(error);
        return;
      }
      String error =
          "Failed to invoker action method `"
              + incomingMessage.getAction()
              + "` at channel `"
              + incomingMessage.getChannel()
              + "` ";
      log.error(error, e);
      session.fail(WsFailureType.UNKNOWN_FAILURE.getValue());
    }
  }

  public boolean supports(String action) {
    return actionMethods.containsKey(ActionType.getFromValue(action));
  }
}
