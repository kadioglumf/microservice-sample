package com.kadioglumf.authservice.handler;

import com.kadioglumf.authservice.core.kafka.DomainEvent;
import com.kadioglumf.authservice.core.kafka.KafkaEventPublisher;
import com.kadioglumf.authservice.event.EmailChangedEvent;
import com.kadioglumf.authservice.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventHandler {
  private final KafkaEventPublisher<DomainEvent> publisher;

  @TransactionalEventListener
  public void handle(EmailChangedEvent event) {
    publisher.publish(event);
  }

  @TransactionalEventListener
  public void handle(UserRegisteredEvent event) {
    publisher.publish(event);
  }
}
