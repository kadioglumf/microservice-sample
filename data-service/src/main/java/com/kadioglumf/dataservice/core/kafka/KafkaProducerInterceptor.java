package com.kadioglumf.dataservice.core.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class KafkaProducerInterceptor implements ProducerInterceptor {

  @Override
  public ProducerRecord onSend(ProducerRecord producerRecord) {
    return producerRecord;
  }

  @Override
  public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {}

  @Override
  public void close() {}

  @Override
  public void configure(Map<String, ?> map) {}
}
