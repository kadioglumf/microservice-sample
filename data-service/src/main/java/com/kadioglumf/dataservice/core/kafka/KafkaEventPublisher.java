package com.kadioglumf.dataservice.core.kafka;

import com.kadioglumf.dataservice.util.UserDeviceDetailsUtils;
import com.kadioglumf.dataservice.util.UserThreadContext;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.KafkaHeaders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Log4j2
public class KafkaEventPublisher<T extends DomainEvent> {

  private final String topic;
  private final KafkaProducer<String, String> kafkaProducer;

  public KafkaEventPublisher(KafkaProducer<String, String> kafkaProducer, String topic) {
    this.topic = topic;
    this.kafkaProducer = kafkaProducer;
  }

  @SneakyThrows
  public void publish(T event) {
    log.info("publishing event: {}", event);
    String eventTypeId = ((DomainEvent) event).getClass().getSimpleName();
    try {
      if (event instanceof DefaultDomainEvent de) {

        if (UserThreadContext.getUser() != null) {
          de.setUserId(UserThreadContext.getUser().getId());
        }
        de.setLocale(UserDeviceDetailsUtils.getUserLocale());
        de.setTransactionTime(LocalDateTime.now());
      }
      String txId = UUID.randomUUID().toString();
      KafkaMessageBuilder body = new KafkaMessageBuilder().body(event);
      String message = body.build();
      String hash = String.valueOf(Objects.hash(message));

      ProducerRecord<String, String> pr =
          new ProducerRecord<>(topic, null, hash, message, new ArrayList<>());
      pr.headers()
          .add(KafkaHeaders.CORRELATION_ID, txId.getBytes())
          .add("event-type-id", eventTypeId.getBytes());
      kafkaProducer.send(pr).get();
    } catch (Exception e) {
      log.error("error: ", e);
      throw e;
    }
  }
}
