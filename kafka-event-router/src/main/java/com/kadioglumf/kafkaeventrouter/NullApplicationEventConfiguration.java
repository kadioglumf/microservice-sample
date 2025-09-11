package com.kadioglumf.kafkaeventrouter;

import java.util.ArrayList;
import java.util.List;

public class NullApplicationEventConfiguration extends ApplicationEventConfiguration {

  NullApplicationEventConfiguration() {}

  @Override
  public List<EventRule> toEventRules() {
    return new ArrayList<>();
  }
}
