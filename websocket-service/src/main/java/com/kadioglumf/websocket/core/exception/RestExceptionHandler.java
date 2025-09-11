package com.kadioglumf.websocket.core.exception;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.secure.SecureException;
import com.kadioglumf.websocket.util.HttpRequestUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {

  private static final String REST_EXCEPTION_HEADER_CAUGHT_TAG =
      "RestExceptionHandler Caught an Exception";

  @Autowired private ExceptionMetaData exceptionMetaData;

  @ExceptionHandler({BusinessException.class, ApplicationException.class})
  public ResponseEntity<RestResponseError> handleBusinessException(Exception ex) {
    return prepareResponseAndLogError(ex, null, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({SecureException.class})
  public ResponseEntity<RestResponseError> handleBusinessException(
      SecureException exc, WebRequest request) {
    ApplicationException be = new ApplicationException(ExceptionConstants.UNAUTHORIZED, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<RestResponseError> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exc, WebRequest request) {
    ApplicationException be =
        new ApplicationException(ExceptionConstants.ERROR_REST_REQUEST_NOT_PROPER, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<RestResponseError> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exc, WebRequest request) {
    ApplicationException be =
        new ApplicationException(ExceptionConstants.ERROR_REST_REQUEST_NOT_PROPER, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<RestResponseError> handleMaxUploadSizeExceededException(
      MaxUploadSizeExceededException exc, WebRequest request) {
    ApplicationException be =
        new ApplicationException(ExceptionConstants.FILE_UPLOAD_SIZE_LIMIT, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<RestResponseError> handleNoResourceFoundException(
      NoResourceFoundException exc, WebRequest request) {
    ApplicationException be = new ApplicationException(ExceptionConstants.NO_RESOURCE_FOUND, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<RestResponseError> handleMaxUploadSizeExceededException(
      BadCredentialsException exc, WebRequest request) {
    ApplicationException be = new ApplicationException(ExceptionConstants.UNEXPECTED, exc);
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestResponseError> catchMethodArgumentNotValidException(
      final MethodArgumentNotValidException exc, WebRequest request) {
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : exc.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : exc.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    BusinessException be =
        new BusinessException(
            ExceptionConstants.METHOD_ARGUMENT_NOT_VALID, exc, errors.toArray(new String[0]));
    return prepareResponseAndLogError(
        be,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<RestResponseError> handleHttpClientErrorException(
      HttpClientErrorException exc, WebRequest request) {
    try {
      var errorCode = exc.getResponseBodyAs(RestResponseError.class).getErrorCode();
      errorCode = errorCode.replace("(BUS)", "");
      BusinessException be = new BusinessException(errorCode, exc);
      return prepareResponseAndLogError(
          be,
          HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
          HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      ApplicationException applicationException =
          new ApplicationException(ExceptionConstants.UNEXPECTED, exc);
      return prepareResponseAndLogError(
          applicationException,
          HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
          HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestResponseError> handleExceptions(Exception exc, WebRequest request) {
    ApplicationException applicationException =
        new ApplicationException(ExceptionConstants.UNEXPECTED, exc);
    return prepareResponseAndLogError(
        applicationException,
        HttpRequestUtils.createNewHttpHeadersWithExtractedMediaTypeFromRequest(request),
        HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<RestResponseError> prepareResponseAndLogError(
      Exception e, HttpHeaders httpHeaders, HttpStatus status) {
    log.error(exceptionMetaData.getDetailedExceptionLog(e, REST_EXCEPTION_HEADER_CAUGHT_TAG));
    ExceptionLog exceptionLog = exceptionMetaData.getExceptionDetail(e);
    String errorPrefix = ExceptionConstants.BUSINESS_ERROR_PREFIX;
    if (e instanceof ApplicationException) {
      errorPrefix = ExceptionConstants.APPLICATION_ERROR_PREFIX;
    }

    var error =
        new RestResponseError(errorPrefix + exceptionLog.getExceptionCode(), exceptionLog.getLog());
    return new ResponseEntity<>(error, httpHeaders, status);
  }
}
