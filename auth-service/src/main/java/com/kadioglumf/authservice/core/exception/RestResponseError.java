package com.kadioglumf.authservice.core.exception;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestResponseError implements Serializable {
  private String errorCode;
  private String errorMessage;
}
