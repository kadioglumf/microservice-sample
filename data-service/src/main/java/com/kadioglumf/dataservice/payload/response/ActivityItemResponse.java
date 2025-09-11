package com.kadioglumf.dataservice.payload.response;

import com.kadioglumf.dataservice.enums.ActivityType;
import java.util.Date;
import lombok.Data;

@Data
public class ActivityItemResponse {
  private String activityId;
  private String valueTo;
  private AuthorUserResponse author;
  private ActivityType type;
  private String value;
  private Date creationDate;
}
