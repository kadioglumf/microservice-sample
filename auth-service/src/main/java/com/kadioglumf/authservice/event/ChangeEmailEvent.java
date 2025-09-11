package com.kadioglumf.authservice.event;

import com.kadioglumf.authservice.core.kafka.DefaultDomainEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ChangeEmailEvent extends DefaultDomainEvent {
  private String email;
  private String newEmail;
  private String name;
  private String otp;
}
