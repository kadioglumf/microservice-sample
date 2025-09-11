package com.kadioglumf.authservice.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "users_address")
public class UserAddressModel extends BaseAddressModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_address_id_generator")
  @SequenceGenerator(
      name = "users_address_id_generator",
      sequenceName = "seq_users_address_id",
      allocationSize = 1)
  private Long id;
}
