package com.kadioglumf.dataservice.payload.response.sociallead;

import com.kadioglumf.dataservice.enums.SocialLeadStatus;
import java.util.Date;
import lombok.Data;

@Data
public class BaseSocialLeadListResponse {
  private Long id;
  private String name;
  private String phone;
  private String email;
  private Date creationDateOfValue;
  private SocialLeadStatus status;
  private String formular;
}
