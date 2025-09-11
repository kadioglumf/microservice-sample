package com.kadioglumf.kafkaeventrouter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventRule {
  private String event;
  private String topicName;

  public EventRule(String event, String topicName) {
    this.event = event;
    this.topicName = topicName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((event == null) ? 0 : event.hashCode());
    result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    EventRule other = (EventRule) obj;
    if (event == null) {
      if (other.event != null) return false;
    } else if (!event.equals(other.event)) return false;
    if (topicName == null) {
      if (other.topicName != null) return false;
    } else if (!topicName.equals(other.topicName)) return false;
    return true;
  }
}
