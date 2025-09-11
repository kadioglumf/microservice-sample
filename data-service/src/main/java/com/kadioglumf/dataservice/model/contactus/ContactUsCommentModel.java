package com.kadioglumf.dataservice.model.contactus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kadioglumf.dataservice.model.AbstractCommentModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "data_contact_us_comment")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class ContactUsCommentModel extends AbstractCommentModel {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_us_comment_id_generator")
  @SequenceGenerator(
      name = "contact_us_comment_id_generator",
      sequenceName = "seq_contact_us_comment",
      allocationSize = 1)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "contact_us_id")
  private ContactUsModel contactUs;

  public ContactUsCommentModel(String message, ContactUsModel contactUs) {
    super(message);
    this.contactUs = contactUs;
  }
}
