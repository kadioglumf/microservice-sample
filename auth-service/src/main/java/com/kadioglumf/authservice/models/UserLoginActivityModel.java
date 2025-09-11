package com.kadioglumf.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "user_login_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
@Builder
public class UserLoginActivityModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "user_login_activity_id_generator")
  @SequenceGenerator(
      name = "user_login_activity_id_generator",
      sequenceName = "seq_user_login_activity_id",
      allocationSize = 1)
  private Long id;

  private Long userId;
}
