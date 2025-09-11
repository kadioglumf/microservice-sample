package com.kadioglumf.dataservice.model.contactus;

import com.kadioglumf.dataservice.converter.GenderConverter;
import com.kadioglumf.dataservice.enums.ContactUsStatus;
import com.kadioglumf.dataservice.enums.Gender;
import com.kadioglumf.dataservice.model.DeviceDetailedAbstractModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Table(name = "data_contact_us")
@SQLRestriction(value = "is_deleted = false")
@EqualsAndHashCode(
    callSuper = false,
    of = {"id"})
public class ContactUsModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_us_id_generator")
  @SequenceGenerator(
      name = "contact_us_id_generator",
      sequenceName = "seq_contact_us",
      allocationSize = 1)
  private Long id;

  private Long userId;
  @NotBlank private String contactRequestId;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ContactUsStatus status = ContactUsStatus.PENDING;

  @NotBlank private String name;
  @NotBlank private String surname;
  @NotBlank private String email;
  @NotBlank private String phone;
  @NotBlank private String subject;

  @NotBlank
  @Lob
  @JdbcTypeCode(SqlTypes.LONGVARCHAR)
  private String message;

  private Boolean isRegistered = false;
  private Boolean isDeleted = false;

  @NotNull
  @Convert(converter = GenderConverter.class)
  private Gender gender;

  private Long assignedUserId;

  @OneToMany(mappedBy = "contactUs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ContactUsActivityModel> activities = new ArrayList<>();

  @OneToMany(mappedBy = "contactUs", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ContactUsCommentModel> comments = new ArrayList<>();
}
