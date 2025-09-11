package com.kadioglumf.dataservice.payload.response;

import com.kadioglumf.dataservice.core.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserDetailsResponse implements BaseDto {
  private Long userId;
  private String name;
  private String surname;

  public String getFullName() {
    if (this.name != null && this.surname != null) {
      return name + " " + surname;
    }
    return null;
  }
}
