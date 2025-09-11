package com.kadioglumf.authservice.payload.response;

import com.kadioglumf.authservice.core.dto.BaseDto;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionResponse implements BaseDto {
  private Map<String, List<String>> permission;
}
