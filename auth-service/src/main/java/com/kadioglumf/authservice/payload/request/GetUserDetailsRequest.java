package com.kadioglumf.authservice.payload.request;

import java.util.Set;
import lombok.Data;

@Data
public class GetUserDetailsRequest {
  private Set<Long> userIds;
}
