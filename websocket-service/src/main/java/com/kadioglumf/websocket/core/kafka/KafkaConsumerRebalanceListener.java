package com.kadioglumf.websocket.core.kafka;

import java.util.Collection;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

public class KafkaConsumerRebalanceListener implements ConsumerRebalanceListener {

  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> partitions) {}

  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {}

  @Override
  public void onPartitionsLost(Collection<TopicPartition> partitions) {
    ConsumerRebalanceListener.super.onPartitionsLost(partitions);
  }
}
