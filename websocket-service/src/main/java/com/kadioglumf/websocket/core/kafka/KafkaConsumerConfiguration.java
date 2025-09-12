package com.kadioglumf.websocket.core.kafka;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ContainerProperties;

@ConditionalOnProperty(name = "project.kafka.enable-consumer", matchIfMissing = true)
@EnableKafka
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaConsumerConfiguration {

  @Value("${project.kafka.consumer.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${project.kafka.consumer.topic.name}")
  private String topic;

  @Value("${project.kafka.consumer.topic.partition-count:1}")
  private int partitionCount;

  @Value("${project.kafka.consumer.topic.replica}")
  private int replica;

  @Value("${project.kafka.consumer.group-id}")
  private String groupId;

  @Value("${project.kafka.consumer.auto-offset-reset:latest}")
  private String autoOffsetReset;

  @Value("${project.kafka.consumer.topic.auto-create-topics}")
  private boolean autoCreateTopics;

  @Value("${project.kafka.consumer.max.poll.records:100}")
  private int maxPollRecords;

  @Value("${project.kafka.poll.timeout.ms:1500}")
  private int pollTimeoutMs;

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
    props.put(
        ConsumerConfig.ISOLATION_LEVEL_CONFIG,
        IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT));

    return props;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> producerConfig = new HashMap<>();
    producerConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return new KafkaAdmin(producerConfig);
  }

  @Bean
  public NewTopic consumerTopic() {
    if (autoCreateTopics) {
      return TopicBuilder.name(topic).partitions(partitionCount).replicas(replica).build();
    }
    return null;
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setBatchListener(true);
    factory.setMissingTopicsFatal(false);
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    factory.getContainerProperties().setPollTimeout(pollTimeoutMs);
    factory
        .getContainerProperties()
        .setConsumerRebalanceListener(new KafkaConsumerRebalanceListener());
    return factory;
  }

  @Bean
  public KafkaEventConsumer<DomainEvent> kafkaEventConsumer() {
    return new KafkaEventConsumer<>();
  }
}
