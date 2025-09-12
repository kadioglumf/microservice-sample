package com.kadioglumf.dataservice.core.kafka;

import com.kadioglumf.dataservice.core.kafka.DomainEvent;

public class KafkaMessageBuilder {
  private StringBuilder json = new StringBuilder();

  public KafkaMessageBuilder body(DomainEvent event) {
    String eventTypeId = event.getClass().getSimpleName();
    eventTypeId(eventTypeId)
        .comma()
        .field("body")
        .semicolon()
        .jsonValue(SimpleJsonDeserializer.convertObjectToJsonData(event));
    return this;
  }

  private KafkaMessageBuilder eventTypeId(String classTypeId) {
    field("eventTypeId").semicolon().field(classTypeId);
    return this;
  }

  private KafkaMessageBuilder field(String field) {
    doublequote();
    json.append(field);
    doublequote();
    return this;
  }

  private KafkaMessageBuilder jsonValue(String field) {
    json.append(field);
    return this;
  }

  private KafkaMessageBuilder comma() {
    json.append(',');
    return this;
  }

  private KafkaMessageBuilder doublequote() {
    json.append('"');
    return this;
  }

  private KafkaMessageBuilder semicolon() {
    json.append(':');
    return this;
  }

  public String build() {
    json.insert(0, '{');
    json.append('}');
    return json.toString();
  }
}
