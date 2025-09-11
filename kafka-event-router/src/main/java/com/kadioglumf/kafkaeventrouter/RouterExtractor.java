package com.kadioglumf.kafkaeventrouter;

import org.apache.kafka.streams.processor.RecordContext;
import org.apache.kafka.streams.processor.TopicNameExtractor;

class RouterExtractor implements TopicNameExtractor<String, Router> {
  @Override
  public String extract(String key, Router value, RecordContext context) {
    return value.getRouteTopic();
  }
}
