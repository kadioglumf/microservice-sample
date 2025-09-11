package com.kadioglumf.authservice.models;

import com.kadioglumf.authservice.enums.ClientTypeEnum;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
@EqualsAndHashCode(callSuper = true)
@SQLRestriction("is_deleted = false")
public class RefreshTokenModel extends DeviceDetailedAbstractModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_id_generator")
  @SequenceGenerator(
      name = "refresh_token_id_generator",
      sequenceName = "seq_refresh_token_id",
      allocationSize = 1)
  private Long id;

  private String refreshToken;
  private Instant expiryDate;

  @ManyToOne
  @JoinColumn(name = "credential_id", referencedColumnName = "id")
  private CredentialsModel credential;

  @Enumerated(EnumType.STRING)
  private ClientTypeEnum clientType;
}
