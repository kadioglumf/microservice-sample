package com.kadioglumf.dataservice.model.sociallead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.dataservice.enums.ActivityType;
import com.kadioglumf.dataservice.model.AbstractActivityModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "data_social_lead_activity")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@SQLRestriction(value = "is_deleted = false")
public class SocialLeadActivityModel extends AbstractActivityModel {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "social_lead_activity_id_generator")
  @SequenceGenerator(
      name = "social_lead_activity_id_generator",
      sequenceName = "seq_social_lead_activity",
      allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "social_lead_id")
  private SocialLeadModel socialLead;

  public SocialLeadActivityModel(
      String value, String valueTo, ActivityType activityType, SocialLeadModel socialLead) {
    super(activityType, value, valueTo);
    this.socialLead = socialLead;
  }
}
