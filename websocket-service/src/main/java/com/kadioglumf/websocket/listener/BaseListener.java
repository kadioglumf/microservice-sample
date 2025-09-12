package com.kadioglumf.websocket.listener;

import com.kadioglumf.websocket.core.kafka.DomainEvent;

public abstract class BaseListener<T extends DomainEvent> {

  public abstract void handle(T event);
}
