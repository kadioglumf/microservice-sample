package com.kadioglumf.dataservice.event;

import com.kadioglumf.dataservice.core.kafka.DefaultDomainEvent;
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
