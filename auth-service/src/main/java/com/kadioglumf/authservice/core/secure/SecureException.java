package com.kadioglumf.authservice.core.secure;

import com.kadioglumf.authservice.core.exception.BaseException;
import com.kadioglumf.authservice.core.exception.SimplifiedExceptionLog;

public class SecureException extends BaseException {

  public SecureException(String exceptionCode, String... exceptionMessageParameters) {
    super(exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }

  public SecureException(
      String exceptionCode, Exception causeException, String... exceptionMessageParameters) {
    super(causeException, exceptionCode, exceptionMessageParameters);
    SimplifiedExceptionLog exceptionDetail = this.getExceptionDetailFromBundle();
    if (exceptionDetail != null) {
      this.setExceptionMessage(exceptionDetail.getLog());
    }
  }
}
