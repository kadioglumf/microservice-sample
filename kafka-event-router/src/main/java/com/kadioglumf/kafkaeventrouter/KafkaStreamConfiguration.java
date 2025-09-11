package com.kadioglumf.kafkaeventrouter;

import static org.apache.kafka.streams.StreamsConfig.EXACTLY_ONCE_V2;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.TopicNameExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class KafkaStreamConfiguration {
  @Autowired private EventRuleConfiguration eventRuleConfiguration;

  @Value("${project.kafka.stream.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${project.kafka.stream.input-topic}")
  private String inputTopic;

  @Value("${project.kafka.stream.application-id}")
  private String applicationId;

  @Value("${project.kafka.stream.client-id}")
  private String clientId;

  @Value("${project.kafka.consumer.auto-offset-reset:latest}")
  private String autoOffsetReset;

  @Value("${project.kafka.consumer.max.poll.records:50}")
  private int maxPollRecords;

  @Value("${project.kafka.fetch.max.wait.ms:2000}")
  private int fetchMaxWaitMs;

  @Value("${project.kafka.retry.backoff.ms:1000}")
  private int retryBackoffMs;

  @Value("${project.kafka.heartbeat.interval.ms:10000}")
  private int heartbeatIntervalMs;

  @Value("${project.kafka.session.timeout.ms:40000}")
  private int sessionTimeout;

  @Value("${project.kafka.max.poll.interval.ms:50000}")
  private int maxPollIntervalMs;

  @Value("${project.kafka.poll.timeout.ms:1500}")
  private int poolTimeoutMs;

  @Value("${project.kafka.producer.acks:all}")
  private String acks;

  @PostConstruct
  public void ensureInputTopicExists() {
    Properties adminProps = new Properties();
    adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

    try (AdminClient adminClient = AdminClient.create(adminProps)) {
      Set<String> existingTopics = adminClient.listTopics().names().get();
      List<NewTopic> topics = new ArrayList<>();
      var allTopics = eventRuleConfiguration.getAllTopics();
      allTopics.add(inputTopic);

      allTopics.forEach(
          topic -> {
            if (!existingTopics.contains(topic)) {
              topics.add(new NewTopic(topic, 3, (short) 1));
            } else {
              log.info("Topic already exists: {}", topic);
            }
          });

      if (!topics.isEmpty()) {
        try {
          adminClient.createTopics(topics).all().get();
          log.info("Created missing topic: {}", topics);
        } catch (ExecutionException e) {
          log.error("Failed to create topics", e);
        }
      }

    } catch (Exception e) {
      log.error("Failed to check or create Kafka topic", e);
    }
  }

  @Bean
  public KafkaStreams kafkaStreams(
      StreamsBuilder builder, @Qualifier("kafkaStreamConfigs") Properties kafkaStreamConfigs) {
    return new KafkaStreams(builder.build(), kafkaStreamConfigs);
  }

  @Bean(destroyMethod = "close")
  public KafkaStream kafkaStream(KafkaStreams streams) {
    return new KafkaStream(streams);
  }

  @Bean
  public RouterRules routerRules() {
    return new RouterRules();
  }

  @Bean
  public StreamsBuilder builder() {
    final StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> stream =
        builder.stream(inputTopic, Consumed.with(Serdes.String(), Serdes.String()));
    stream.flatMap(mapper()).to(executor(), Produced.with(Serdes.String(), serde()));
    return builder;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public TopicNameExtractor<String, Router> executor() {
    return new RouterExtractor();
  }

  @Bean
  public Serde<Router> serde() {
    return new RouterSerde();
  }

  @Bean
  public KeyValueMapper<String, String, Iterable<KeyValue<String, Router>>> mapper() {
    return new RouterMapper();
  }

  @Bean("kafkaStreamConfigs")
  public Properties kafkaStreamConfigs() {
    Properties kafkaStreamConfigs = new Properties();
    kafkaStreamConfigs.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
    kafkaStreamConfigs.put(StreamsConfig.CLIENT_ID_CONFIG, clientId);
    kafkaStreamConfigs.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    kafkaStreamConfigs.put(
        StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    kafkaStreamConfigs.put(
        StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    kafkaStreamConfigs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    kafkaStreamConfigs.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
    kafkaStreamConfigs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
    kafkaStreamConfigs.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs);
    kafkaStreamConfigs.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackoffMs);
    kafkaStreamConfigs.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, heartbeatIntervalMs);
    kafkaStreamConfigs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
    kafkaStreamConfigs.put(StreamsConfig.POLL_MS_CONFIG, poolTimeoutMs);
    kafkaStreamConfigs.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
    kafkaStreamConfigs.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), acks);
    kafkaStreamConfigs.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, EXACTLY_ONCE_V2);
    kafkaStreamConfigs.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 200);
    kafkaStreamConfigs.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_DOC, 0);
    return kafkaStreamConfigs;
  }
}
