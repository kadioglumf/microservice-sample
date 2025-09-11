package com.kadioglumf.dataservice.core.exception;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.ApplicationMetaData;
import com.kadioglumf.dataservice.core.secure.SecureException;
import com.kadioglumf.dataservice.util.UserDeviceDetailsUtils;
import jakarta.annotation.Resource;
import java.util.Locale;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class ExceptionMetaData {

  @Resource(name = "applicationExceptionMessageSource")
  private MessageSource applicationExceptionMessageSource;

  @Resource(name = "businessExceptionMessageSource")
  private MessageSource businessExceptionMessageSource;

  @Autowired private ApplicationMetaData applicationMetaData;

  public ExceptionMetaData() {}

  public ExceptionLog getExceptionDetail(Exception e) {
    if (e instanceof BusinessException) {
      return this.getExceptionMessagesFromSource(
          this.businessExceptionMessageSource, (BusinessException) e);
    } else if (e instanceof ApplicationException) {
      return this.getExceptionMessagesFromSource(
          this.applicationExceptionMessageSource, (ApplicationException) e);
    } else if (e instanceof SecureException) {
      return this.getExceptionMessagesFromSource(
          this.applicationExceptionMessageSource, (SecureException) e);
    } else {
      ApplicationException ae = new ApplicationException(ExceptionConstants.UNEXPECTED, e);
      return getExceptionMessagesFromSource(applicationExceptionMessageSource, ae);
    }
  }

  public SimplifiedExceptionLog getExceptionMessagesFromSource(BaseException e) {
    SimplifiedExceptionLog toReturn = new SimplifiedExceptionLog();
    toReturn.setExceptionCode(e.getExceptionCode());
    Locale locale = UserDeviceDetailsUtils.getUserLocale();

    String defaultMessage = getDefaultMessage(locale) + "(" + e.getExceptionCode() + ")";

    MessageSource source;
    if (e instanceof BusinessException) {
      source = this.businessExceptionMessageSource;
    } else {
      source = this.applicationExceptionMessageSource;
    }

    toReturn.setLog(
        source.getMessage(
            e.getExceptionCode(), e.getExceptionMessageParameters(), defaultMessage, locale));
    return toReturn;
  }

  private String getDefaultMessage(Locale locale) {
    return switch (locale.getLanguage()) {
      case "fr" ->
          ExceptionBundleConstants
              .MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_FR;
      case "it" ->
          ExceptionBundleConstants
              .MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_IT;
      case "en" ->
          ExceptionBundleConstants
              .MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_EN;
      default ->
          ExceptionBundleConstants
              .MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES;
    };
  }

  private ExceptionLog getExceptionMessagesFromSource(MessageSource source, BaseException e) {
    Locale locale = UserDeviceDetailsUtils.getUserLocale();

    String defaultMessage = getDefaultMessage(locale) + "(" + e.getExceptionCode() + ")";
    return (new ExceptionLog.ExceptionLogBuilder()
            .exceptionCode(e.getExceptionCode())
            .log(
                source.getMessage(
                    e.getExceptionCode(),
                    e.getExceptionMessageParameters(),
                    defaultMessage,
                    locale))
            .cause(e.getCause())
            .exceptionStackTrace(ExceptionUtils.getStackTrace(e)))
        .build();
  }

  public String getDetailedExceptionLog(
      Exception e, ExceptionLog exceptionDetail, String headerTag) {
    String whichClass = null;
    int whichLine = 0;
    StackTraceElement[] stackTrace = e.getStackTrace();
    if (stackTrace != null && stackTrace.length > 0) {
      StackTraceElement stackTraceElement = stackTrace[0];
      whichClass = stackTraceElement.getClassName();
      whichLine = stackTraceElement.getLineNumber();
    }

    String applicationName =
        StringUtils.isBlank(applicationMetaData.getApplicationName())
            ? "NA"
            : applicationMetaData.getApplicationName();
    String exceptionCode =
        StringUtils.isBlank(exceptionDetail.getExceptionCode())
            ? ""
            : exceptionDetail.getExceptionCode();
    StringBuilder errorLogInDetail = new StringBuilder(createHeaderPrompt(headerTag));
    errorLogInDetail
        .append("EXCEPTION SEARCHCODE : ")
        .append(applicationName.toUpperCase())
        .append("-")
        .append(exceptionCode.toUpperCase())
        .append(System.lineSeparator());
    errorLogInDetail.append("EXCEPTION CLASS : ").append(whichClass).append(System.lineSeparator());
    errorLogInDetail.append("EXCEPTION LINE : ").append(whichLine).append(System.lineSeparator());
    errorLogInDetail
        .append("EXCEPTION TYPE : ")
        .append(e.getClass().getName())
        .append(System.lineSeparator());
    errorLogInDetail
        .append("EXCEPTION CODE :")
        .append(exceptionCode.toUpperCase())
        .append(System.lineSeparator());
    errorLogInDetail
        .append("EXCEPTION LOG : ")
        .append(exceptionDetail.getLog())
        .append(System.lineSeparator());

    if (e.getCause() != null) {
      errorLogInDetail
          .append("CAUSE MESSAGE : ")
          .append(ExceptionUtils.getRootCauseMessage(e))
          .append(System.lineSeparator());
    }

    errorLogInDetail
        .append("--------------------------------------------------------")
        .append(System.lineSeparator());
    return errorLogInDetail.toString();
  }

  private String createHeaderPrompt(String headerTag) {
    if (StringUtils.isBlank(headerTag)) {
      return new StringBuilder()
          .append(System.lineSeparator())
          .append("--------------------------------------------------------")
          .append(System.lineSeparator())
          .toString();
    }
    int dashLength = headerTag.length();
    StringBuilder dash = new StringBuilder();
    IntStream.rangeClosed(1, dashLength + 10).forEach(i -> dash.append("-"));
    StringBuilder header =
        new StringBuilder()
            .append(System.lineSeparator())
            .append(dash)
            .append(System.lineSeparator())
            .append("------ ")
            .append(headerTag)
            .append(" ------")
            .append(System.lineSeparator())
            .append(dash)
            .append(System.lineSeparator());
    return header.toString();
  }

  public String getDetailedExceptionLog(Exception e, String headerTag) {
    ExceptionLog exceptionDetail = getExceptionDetail(e);
    return getDetailedExceptionLog(e, exceptionDetail, headerTag);
  }

  public String getDetailedExceptionLog(Exception e) {
    ExceptionLog exceptionDetail = getExceptionDetail(e);
    return getDetailedExceptionLog(e, exceptionDetail, null);
  }
}
