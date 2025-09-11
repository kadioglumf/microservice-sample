package com.kadioglumf.authservice.models;

import com.kadioglumf.authservice.models.generator.annotation.LoggedUserIdGenerator;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractModel implements Serializable {

  @DateTimeFormat
  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Date creationDate;

  @DateTimeFormat @UpdateTimestamp private Date lastUpdateDate;

  @Column(name = "create_by_user_id", updatable = false)
  @LoggedUserIdGenerator
  private Long createBy;

  @Column(name = "update_by_user_id")
  @LoggedUserIdGenerator
  private Long updateBy;

  private boolean isDeleted = false;
}
