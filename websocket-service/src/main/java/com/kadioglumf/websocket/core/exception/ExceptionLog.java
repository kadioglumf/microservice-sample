package com.kadioglumf.websocket.core.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionLog {
  private String exceptionCode;
  private String log;
  private String exceptionStackTrace;
  private Throwable cause;
}
