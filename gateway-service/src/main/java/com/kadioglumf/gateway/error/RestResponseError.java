package com.kadioglumf.gateway.error;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponseError implements Serializable {
  private String errorCode;
  private String errorMessage;
}
