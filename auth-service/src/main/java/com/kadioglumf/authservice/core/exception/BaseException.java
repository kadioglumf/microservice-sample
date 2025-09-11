package com.kadioglumf.authservice.core.exception;

import com.kadioglumf.authservice.core.config.SpringBeanContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseException extends RuntimeException {

  private String exceptionCode;
  private String exceptionMessage;
  private String[] exceptionMessageParameters;

  public BaseException(String exceptionCode, String[] exceptionMessageParameters) {
    this.exceptionCode = exceptionCode;
    this.exceptionMessageParameters = exceptionMessageParameters;
  }

  public BaseException(
      Throwable causeException, String exceptionCode, String[] exceptionMessageParameters) {
    super(causeException);
    this.exceptionCode = exceptionCode;
    this.exceptionMessageParameters = exceptionMessageParameters;
  }

  public SimplifiedExceptionLog getExceptionDetailFromBundle() {
    ExceptionMetaData emd = SpringBeanContext.getBean(ExceptionMetaData.class);
    return emd.getExceptionMessagesFromSource(this);
  }
}
