package com.kadioglumf.dataservice.model;

import com.kadioglumf.dataservice.enums.ActivityType;
import com.kadioglumf.dataservice.model.generator.annotation.LoggedUserIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class AbstractActivityModel extends DeviceDetailedAbstractModel {

  @Column(nullable = false, unique = true)
  private String activityId;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private ActivityType type;

  private String value;

  private String valueTo;

  @LoggedUserIdGenerator private Long authorId;

  public AbstractActivityModel(ActivityType type, String value, String valueTo) {
    this.type = type;
    this.value = value;
    this.valueTo = valueTo;
    this.activityId = UUID.randomUUID().toString();
  }
}
