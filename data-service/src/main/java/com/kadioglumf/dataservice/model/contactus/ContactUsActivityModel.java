package com.kadioglumf.dataservice.model.contactus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.dataservice.enums.ActivityType;
import com.kadioglumf.dataservice.model.AbstractActivityModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "data_contact_us_activity")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class ContactUsActivityModel extends AbstractActivityModel {

  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "contact_us_activity_id_generator")
  @SequenceGenerator(
      name = "contact_us_activity_id_generator",
      sequenceName = "seq_contact_us_activity",
      allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contact_us_id")
  private ContactUsModel contactUs;

  public ContactUsActivityModel(
      String value, String valueTo, ActivityType activityType, ContactUsModel contactUs) {
    super(activityType, value, valueTo);
    this.contactUs = contactUs;
  }
}
