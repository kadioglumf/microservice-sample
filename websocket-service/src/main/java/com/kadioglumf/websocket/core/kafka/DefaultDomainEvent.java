package com.kadioglumf.websocket.core.kafka;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
@Accessors(chain = true)
public class DefaultDomainEvent implements DomainEvent, Serializable {
  private Locale locale;
  private LocalDateTime transactionTime;
  private Long userId;
  private long offset;

  public DefaultDomainEvent() {
    locale = Locale.of("tr", "TR");
  }
}
