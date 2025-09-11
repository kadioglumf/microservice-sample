package com.kadioglumf.authservice.handler;

import com.kadioglumf.authservice.core.kafka.DomainEvent;
import com.kadioglumf.authservice.core.kafka.KafkaEventPublisher;
import com.kadioglumf.authservice.event.ChangeEmailEvent;
import com.kadioglumf.authservice.event.ForgotPasswordEmailEvent;
import com.kadioglumf.authservice.event.SendEmailActivationEvent;
import com.kadioglumf.authservice.event.UserInfoChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventHandler {
  private final KafkaEventPublisher<DomainEvent> publisher;

  @TransactionalEventListener
  public void handle(ChangeEmailEvent event) {
    publisher.publish(event);
  }
}
