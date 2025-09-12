package com.kadioglumf.dataservice.core.kafka;

import com.kadioglumf.dataservice.core.kafka.DomainEvent;
import com.kadioglumf.dataservice.core.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty(name = "project.kafka.enable-producer", matchIfMissing = true)
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaPublisherConfiguration {
  @Value("${project.kafka.producer.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${project.kafka.producer.topic.name}")
  private String topic;

  @Value("${project.kafka.producer-topic.partition-count:1}")
  private int partitionCount;

  @Value("${project.kafka.producer.topic.replica}")
  private int replica;

  @Value("${project.kafka.producer.retries:15}")
  private int retries;

  @Value("${max.in.flight.requests.per.connection:1}")
  private int maxInFlightRequestsPerConnection;

  @Value("${linger.ms:5}")
  private int lingerMs;

  @Value("${acks:all}")
  private String acks;

  @Value("${batch.size:327680}")
  private int batchSize;

  @Value("${request.timeout.ms:15000}")
  private int requestTimeoutMs;

  @Value("${retry.backoff.ms:1000}")
  private int retryBackoffMs;

  @Value("${delivery.timeout.ms:60000}")
  private int deliveryTimeoutMs;

  @Bean
  public Map<String, Object> producerConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.RETRIES_CONFIG, retries);
    props.put(
        ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection);
    props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
    props.put(ProducerConfig.ACKS_CONFIG, acks);
    props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeoutMs);
    props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
    props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, KafkaProducerInterceptor.class.getName());
    props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs);
    return props;
  }

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> producerConfig = producerConfig();
    producerConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return new KafkaAdmin(producerConfig);
  }

  @Bean
  public NewTopic producerTopic() {
    return TopicBuilder.name(topic).partitions(partitionCount).replicas(replica).build();
  }

  @Bean
  public KafkaProducer<String, String> kafkaProducer() {
    return new KafkaProducer<>(producerConfig());
  }

  @Bean
  public KafkaEventPublisher<DomainEvent> kafkaEventPublisher() {
    return new KafkaEventPublisher<>(kafkaProducer(), topic);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfig());
  }
}
