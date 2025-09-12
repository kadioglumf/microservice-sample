package com.kadioglumf.websocket.event;

import com.kadioglumf.websocket.core.kafka.DefaultDomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString
public class EmailChangedEvent extends DefaultDomainEvent {
  private String email;
}
