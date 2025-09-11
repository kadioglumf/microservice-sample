package com.kadioglumf.authservice.core.kafka;

import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;

@Log4j2
public class KafkaEventConsumer<T extends DomainEvent> {
  @Autowired private ApplicationEventPublisher applicationEventPublisher;
  @Autowired private Environment environment;
  @Autowired private EventTypeRegistry eventTypeRegistry;
  private Consumer<?, ?> kafkaConsumer;

  public KafkaEventConsumer() {}

  @KafkaListener(
      topics = "${project.kafka.consumer.topic.name}",
      autoStartup = "${project.kafka.consumer.auto-start:true}")
  private void consumeNonOrderMatch(
      ConsumerRecords<String, String> consumerRecords, Consumer<?, ?> consumer) {
    log.error("test consumeNonOrderMatch");
    String topic = environment.getProperty("project.kafka.consumer.topic.name");
    consumeRecord(consumerRecords, consumer, topic);
  }

  private void consumeRecord(
      ConsumerRecords<String, String> consumerRecords, Consumer<?, ?> consumer, String topic) {
    kafkaConsumer = consumer;
    consumerRecords.forEach(
        record -> {
          DomainEvent event = null;
          try {
            log.info("event consumed {}", record.value());
            var jsonNode = JsonParser.parseString(record.value()).getAsJsonObject();
            var body = jsonNode.getAsJsonObject("body").toString();
            var eventTypeId = jsonNode.get("eventTypeId").getAsString();

            Class<? extends DomainEvent> clazz = eventTypeRegistry.resolve(eventTypeId);
            event = SimpleJsonDeserializer.convertJsonDataToObject(body, clazz);
            applicationEventPublisher.publishEvent(event);
          } catch (Exception e) {
            log.error("error: ", e);
          }
        });
    consumer.commitSync();
  }
}
