package com.kadioglumf.kafkaeventrouter;

import static java.util.Objects.nonNull;

import org.apache.kafka.streams.KafkaStreams;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaStream implements AutoCloseable {

  private final KafkaStreams streams;

  public KafkaStream(KafkaStreams streams) {
    this.streams = streams;
  }

  @Override
  public void close() throws Exception {
    if (nonNull(streams)) {
      streams.close();
    }
  }

  @EventListener(classes = ApplicationReadyEvent.class)
  public void start() {
    if (nonNull(streams) && !streams.state().isRunningOrRebalancing()) {
      streams.cleanUp();
      streams.start();
    }
  }
}
