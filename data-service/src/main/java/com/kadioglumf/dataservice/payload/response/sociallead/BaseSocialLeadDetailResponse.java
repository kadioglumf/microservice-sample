package com.kadioglumf.dataservice.payload.response.sociallead;

import com.kadioglumf.dataservice.enums.SocialLeadStatus;
import com.kadioglumf.dataservice.payload.response.ActivityItemResponse;
import com.kadioglumf.dataservice.payload.response.CommentItemResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class BaseSocialLeadDetailResponse {
  private Long id;
  private String name;
  private String phone;
  private String email;
  private Date creationDateOfValue;
  private SocialLeadStatus status;
  private String formular;
  private List<CommentItemResponse> comments = new ArrayList<>();
  private List<ActivityItemResponse> activities = new ArrayList<>();
}
