package com.kadioglumf.authservice.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "credentials_users",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
@Builder
public class UserModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_users_id_generator")
  @SequenceGenerator(
      name = "credentials_users_id_generator",
      sequenceName = "seq_credentials_users_id",
      allocationSize = 1)
  private Long id;

  @JsonFormat(pattern = "dd.MM.yyyy")
  private LocalDate birthDay;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_address_id")
  private UserAddressModel address;

  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "credential_id", referencedColumnName = "id")
  private CredentialsModel credential;
}
