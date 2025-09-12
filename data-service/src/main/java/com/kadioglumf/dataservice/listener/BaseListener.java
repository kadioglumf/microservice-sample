package com.kadioglumf.dataservice.listener;

import com.kadioglumf.dataservice.core.kafka.DomainEvent;

public abstract class BaseListener<T extends DomainEvent> {

  public abstract void handle(T event);
}
