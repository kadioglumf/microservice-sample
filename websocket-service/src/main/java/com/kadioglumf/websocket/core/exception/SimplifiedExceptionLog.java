package com.kadioglumf.websocket.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
