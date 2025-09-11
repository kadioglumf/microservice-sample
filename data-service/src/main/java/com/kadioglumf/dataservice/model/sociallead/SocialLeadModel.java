package com.kadioglumf.dataservice.model.sociallead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.dataservice.enums.SocialLeadStatus;
import com.kadioglumf.dataservice.model.DeviceDetailedAbstractModel;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

@Entity
@Table(name = "data_social_lead")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SocialLeadModel extends DeviceDetailedAbstractModel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "social_lead_id_generator")
  @SequenceGenerator(
      name = "social_lead_id_generator",
      sequenceName = "seq_social_lead",
      allocationSize = 1)
  private Long id;

  private String name;
  private String phone;
  private String email;
  private Date creationDateOfValue;
  private String formular;
  private Long assignedPartnerId;

  @OneToMany(mappedBy = "socialLead", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SocialLeadActivityModel> activities = new ArrayList<>();

  @OneToMany(mappedBy = "socialLead", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SocialLeadCommentModel> comments = new ArrayList<>();

  @Enumerated(value = EnumType.STRING)
  private SocialLeadStatus status;

  public void addActivity(SocialLeadActivityModel activity) {
    if (CollectionUtils.isEmpty(activities)) {
      activities = new ArrayList<>();
    }
    activities.add(activity);
  }

  public void addComment(SocialLeadCommentModel comment) {
    if (CollectionUtils.isEmpty(comments)) {
      comments = new ArrayList<>();
    }
    comments.add(comment);
  }
}
