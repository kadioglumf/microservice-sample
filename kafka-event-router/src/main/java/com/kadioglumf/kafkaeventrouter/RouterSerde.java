package com.kadioglumf.kafkaeventrouter;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class RouterSerde implements Serde<Router> {
  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {}

  @Override
  public void close() {}

  @Override
  public Serializer<Router> serializer() {
    return new Serializer<Router>() {
      @Override
      public void configure(Map<String, ?> configs, boolean isKey) {}

      @Override
      public byte[] serialize(String topic, Router data) {
        return data.getValue().getBytes();
      }

      @Override
      public void close() {}
    };
  }

  @Override
  public Deserializer<Router> deserializer() {
    return null;
  }
}
