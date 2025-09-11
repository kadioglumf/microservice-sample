package com.kadioglumf.authservice.event;

import com.kadioglumf.authservice.core.kafka.DefaultDomainEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class UserInfoChangedEvent extends DefaultDomainEvent {
  private String email;
  private Long id;
}
