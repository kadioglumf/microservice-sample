package com.kadioglumf.kafkaeventrouter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class RouterRules {

  private Map<String, List<String>> routerRules = new HashMap<>();

  public RouterRules() {
    loadRules();
  }

  public List<String> rules(String eventTypeId) {
    return routerRules.entrySet().stream()
        .filter(e -> e.getKey().equals(eventTypeId))
        .map(Map.Entry::getValue)
        .findFirst()
        .orElse(new ArrayList<>());
  }

  public Optional<String> getRulesJson() {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return Optional.of(objectMapper.writeValueAsString(routerRules));
    } catch (JsonProcessingException e) {
      return Optional.empty();
    }
  }

  public void loadRules() {
    routerRules.clear();
    ClassPathResource resource = null;
    try {
      resource = new ClassPathResource("rules.json");
      ObjectMapper objectMapper = new ObjectMapper();
      try (InputStream inputStream = resource.getInputStream()) {
        routerRules = objectMapper.readValue(inputStream, HashMap.class);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      resource = null;
    }
  }

  @Override
  public String toString() {
    return "RouterRules{" + "routerRules=" + routerRules + '}';
  }
}
