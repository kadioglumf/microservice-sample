package com.kadioglumf.authservice.payload.response;

import com.kadioglumf.authservice.core.dto.BaseDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse implements BaseDto {
  private String message;
}
