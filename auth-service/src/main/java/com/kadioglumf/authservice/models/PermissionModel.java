package com.kadioglumf.authservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = {"permission"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SQLRestriction("is_deleted = false")
public class PermissionModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_id_generator")
  @SequenceGenerator(
      name = "permissions_id_generator",
      sequenceName = "seq_permissions_id",
      allocationSize = 1)
  private Long id;

  @Column(nullable = false)
  private String authorizationName;

  @Column(nullable = false)
  private String permission;

  private String description;
}
