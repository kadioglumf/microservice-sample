package com.kadioglumf.authservice.core.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedExceptionLog {
  private String exceptionCode;
  private String log;

  public String toString() {
    return "SimplifiedExceptionLog.SimplifiedExceptionLogBuilder(exceptionCode="
        + this.exceptionCode
        + ", log="
        + this.log
        + ")";
  }
}
