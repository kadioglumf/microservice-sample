package com.kadioglumf.dataservice.core.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends BaseException {

  public ApplicationException(String exceptionCode, String... exceptionMessageParameters) {
    super(exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }

  public ApplicationException(
      String exceptionCode, Exception causeException, String... exceptionMessageParameters) {
    super(causeException, exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }
}
