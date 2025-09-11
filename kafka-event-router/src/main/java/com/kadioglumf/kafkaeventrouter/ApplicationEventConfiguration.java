package com.kadioglumf.kafkaeventrouter;

import java.util.List;
import java.util.stream.Collectors;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ApplicationEventConfiguration {
  private String consumerTopicName;
  private List<String> events;

  List<EventRule> toEventRules() {
    return events.stream()
        .map(e -> new EventRule(e, consumerTopicName))
        .collect(Collectors.toList());
  }
}
