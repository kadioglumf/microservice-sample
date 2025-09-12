package com.kadioglumf.dataservice.core.kafka;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.kadioglumf.dataservice.core.kafka.DomainEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Locale;

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
