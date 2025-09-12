package com.kadioglumf.websocket.core.kafka;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

@Component
public class EventTypeRegistry {

  private final Map<String, Class<? extends DomainEvent>> eventTypeMap = new HashMap<>();

  @PostConstruct
  public void init() {
    Reflections reflections = new Reflections("com.kadioglumf.websocket");

    Set<Class<? extends DomainEvent>> subTypes = reflections.getSubTypesOf(DomainEvent.class);

    for (Class<? extends DomainEvent> clazz : subTypes) {
      eventTypeMap.put(clazz.getSimpleName(), clazz);
    }

    System.out.println("EventType Map yüklendi: " + eventTypeMap.keySet());
  }

  public Class<? extends DomainEvent> resolve(String simpleName) {
    return eventTypeMap.get(simpleName);
  }
}
