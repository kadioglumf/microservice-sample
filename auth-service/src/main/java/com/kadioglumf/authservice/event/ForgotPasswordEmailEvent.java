package com.kadioglumf.authservice.event;

import com.kadioglumf.authservice.core.kafka.DefaultDomainEvent;
import java.util.Set;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ForgotPasswordEmailEvent extends DefaultDomainEvent {
  private String email;
  private String name;
  private String otp;
  private Set<String> roles;
}
