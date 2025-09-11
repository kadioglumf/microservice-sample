package com.kadioglumf.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.authservice.converter.GenderConverter;
import com.kadioglumf.authservice.core.enums.RoleTypeEnum;
import com.kadioglumf.authservice.enums.GenderEnum;
import com.kadioglumf.authservice.enums.OAuth2ProviderEnum;
import com.kadioglumf.authservice.enums.UserActivityEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "credentials",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
@Builder
public class CredentialsModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credentials_id_generator")
  @SequenceGenerator(
      name = "credentials_id_generator",
      sequenceName = "seq_credentials_id",
      allocationSize = 1)
  private Long id;

  @Size(max = 25)
  private String name;

  @Size(max = 25)
  private String surname;

  @NotBlank
  @Size(max = 100)
  @Email
  private String email;

  @Size(max = 120)
  private String password;

  @Size(max = 20)
  private String mobile;

  @Size(max = 30)
  private String nationality;

  @Column(length = 10)
  @Convert(converter = GenderConverter.class)
  private GenderEnum gender;

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private UserActivityEnum activity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OAuth2ProviderEnum provider;

  @ElementCollection(targetClass = RoleTypeEnum.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "credentials_roles", joinColumns = @JoinColumn(name = "credential_id"))
  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Set<RoleTypeEnum> roles;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "credentials_permissions",
      joinColumns = @JoinColumn(name = "credential_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<PermissionModel> permissions = new HashSet<>();

  @OneToMany(mappedBy = "credential", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<RefreshTokenModel> refreshTokens;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "credential")
  private UserModel user;
}
