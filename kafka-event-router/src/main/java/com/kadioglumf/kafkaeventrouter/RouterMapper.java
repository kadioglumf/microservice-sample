package com.kadioglumf.kafkaeventrouter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class RouterMapper
    implements KeyValueMapper<String, String, Iterable<KeyValue<String, Router>>> {

  private String deadLettterTopicName = "dead-letter-topic";
  @Autowired private ObjectMapper objectMapper;
  @Autowired private RouterRules routerRules;

  @Override
  public Iterable<KeyValue<String, Router>> apply(String key, String value) {
    List<KeyValue<String, Router>> result = new ArrayList<>();
    JsonNode jsonNode = null;
    try {
      jsonNode = objectMapper.readTree(value);
    } catch (Exception e) {
      e.printStackTrace();
      return result;
    }
    String eventTypeId = jsonNode.get("eventTypeId").asText();
    List<String> toTopics = routerRules.rules(eventTypeId);
    if (Objects.isNull(toTopics) || toTopics.isEmpty()) {

      result.add(
          KeyValue.pair(
              key, Router.builder().routeTopic(deadLettterTopicName).value(value).build()));
    } else {
      for (String topic : toTopics) {
        result.add(KeyValue.pair(topic, Router.builder().routeTopic(topic).value(value).build()));
      }
    }
    return result;
  }
}
