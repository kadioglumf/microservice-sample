package com.kadioglumf.websocket.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends BaseException {

  public BusinessException(String exceptionCode, String... exceptionMessageParameters) {
    super(exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }

  public BusinessException(
      String exceptionCode, Exception causeException, String... exceptionMessageParameters) {
    super(causeException, exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }
}
