package com.kadioglumf.dataservice.model.sociallead;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.dataservice.model.AbstractCommentModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "data_social_lead_comment")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@SQLRestriction(value = "is_deleted = false")
public class SocialLeadCommentModel extends AbstractCommentModel {
  @Id
  @GeneratedValue(
      strategy = GenerationType.SEQUENCE,
      generator = "social_lead_comment_id_generator")
  @SequenceGenerator(
      name = "social_lead_comment_id_generator",
      sequenceName = "seq_social_lead_comment",
      allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "social_lead_id")
  private SocialLeadModel socialLead;

  public SocialLeadCommentModel(String message, SocialLeadModel socialLead) {
    super(message);
    this.socialLead = socialLead;
  }
}
