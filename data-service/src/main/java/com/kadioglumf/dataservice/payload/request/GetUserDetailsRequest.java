package com.kadioglumf.dataservice.payload.request;

import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsRequest {
  private Set<Long> userIds;
}
