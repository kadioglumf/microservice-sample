package com.kadioglumf.dataservice.model;

import com.kadioglumf.dataservice.model.generator.annotation.LoggedUserIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractCommentModel extends DeviceDetailedAbstractModel {

  @Column(nullable = false, unique = true)
  private String commentId;

  @Column(nullable = false)
  private String message;

  @Column(nullable = false)
  @LoggedUserIdGenerator
  private Long authorId;

  public AbstractCommentModel(String message) {
    this.message = message;
    this.commentId = UUID.randomUUID().toString();
  }
}
