package com.kadioglumf.kafkaeventrouter;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EventRuleConfiguration {
  @Value("${apps.email-service.consumer-topic-name}")
  private String emailServiceConsumerTopicName;

  @Value("${apps.email-service.events}")
  private String emailServiceEvents;

  @Value("${apps.data-service.consumer-topic-name}")
  private String dataServiceConsumerTopicName;

  @Value("${apps.data-service.events}")
  private String dataServiceEvents;

  public ApplicationEventConfiguration getEmailServiceConfiguration() {
    return getConfiguration(emailServiceConsumerTopicName, emailServiceEvents);
  }

  public ApplicationEventConfiguration getDataServiceConfiguration() {
    return getConfiguration(dataServiceConsumerTopicName, dataServiceEvents);
  }

  public List<String> getTopicsByEvent(String event) {
    return getAllEventRules().stream()
        .filter(e -> e.getEvent().equals(event))
        .map(EventRule::getTopicName)
        .collect(toList());
  }

  public List<EventRule> getAllEventRules() {
    var rules = new ArrayList<EventRule>();
    rules.addAll(getEmailServiceConfiguration().toEventRules());
    rules.addAll(getDataServiceConfiguration().toEventRules());
    return rules;
  }

  public Set<String> getAllTopics() {
    return getAllEventRules().stream().map(EventRule::getTopicName).collect(Collectors.toSet());
  }

  private ApplicationEventConfiguration getConfiguration(String topicName, String events) {
    if (nonNull(topicName) && nonNull(events)) {
      return ApplicationEventConfiguration.builder()
          .consumerTopicName(topicName)
          .events(List.of(events.split(",")))
          .build();
    }
    return new NullApplicationEventConfiguration();
  }
}
