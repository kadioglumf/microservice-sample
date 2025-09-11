package com.kadioglumf.dataservice.model;

import com.kadioglumf.dataservice.model.generator.annotation.LoggedUserIpGenerator;
import com.kadioglumf.dataservice.model.generator.annotation.OriginGenerator;
import com.kadioglumf.dataservice.model.generator.annotation.OriginIpGenerator;
import com.kadioglumf.dataservice.model.generator.annotation.UserAgentGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetailedAbstractModel extends AbstractModel {

  @Column(updatable = false)
  @LoggedUserIpGenerator
  private String createdByIpAddr;

  @LastModifiedBy @LoggedUserIpGenerator private String updatedByIpAddr;

  @Column(updatable = false)
  @OriginIpGenerator
  private String originIpAddr;

  @OriginGenerator private String origin;

  @UserAgentGenerator private String userAgent;
}
