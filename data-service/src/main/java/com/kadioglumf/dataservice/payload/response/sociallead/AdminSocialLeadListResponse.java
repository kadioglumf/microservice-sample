package com.kadioglumf.dataservice.payload.response.sociallead;

import lombok.Data;

@Data
public class AdminSocialLeadListResponse extends BaseSocialLeadListResponse {
  private String assignedPartnerName;
  private String assignedPartnerId;
}
