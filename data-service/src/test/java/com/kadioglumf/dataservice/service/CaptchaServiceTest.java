package com.kadioglumf.dataservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.kadioglumf.dataservice.base.BaseTest;
import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.payload.response.captcha.CaptchaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class CaptchaServiceTest extends BaseTest {

  @InjectMocks private CaptchaService captchaService;
  @Mock private RestClient restClient;

  private RestClient.RequestBodyUriSpec requestBodySpec;
  private RestClient.RequestBodySpec requestHeadersSpec;
  private RestClient.ResponseSpec responseSpec;

  @BeforeEach
  public void beforeEach() {
    super.beforeEach();
    captchaService.recaptchaSecret = "test-secret";
    captchaService.recaptchaVerifyUrl = "http://test-url.com";

    requestBodySpec = mock(RestClient.RequestBodyUriSpec.class);
    requestHeadersSpec = mock(RestClient.RequestBodySpec.class);
    responseSpec = mock(RestClient.ResponseSpec.class);
  }

  @Test
  @DisplayName("When called then verify recaptcha")
  void verify_WhenCalled_ReturnsAllCantons() {
    CaptchaResponse mockResponse = new CaptchaResponse();
    mockResponse.setSuccess(true);

    when(restClient.post()).thenReturn(requestBodySpec);
    when(requestBodySpec.uri(captchaService.recaptchaVerifyUrl)).thenReturn(requestBodySpec);
    when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.toEntity(CaptchaResponse.class))
        .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

    assertDoesNotThrow(() -> captchaService.verify("valid-response"));
  }

  @Test
  void verify_NullResponse_ThrowsBusinessException() {
    when(restClient.post()).thenReturn(requestBodySpec);
    when(requestBodySpec.uri(captchaService.recaptchaVerifyUrl)).thenReturn(requestBodySpec);
    when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.toEntity(CaptchaResponse.class))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> captchaService.verify("invalid-response"));
    assertEquals(ExceptionConstants.CAPTCHA_INVALID, exception.getExceptionCode());
  }

  @Test
  void verify_NullSuccessResponse_ThrowsBusinessException() {
    when(restClient.post()).thenReturn(requestBodySpec);
    when(requestBodySpec.uri(captchaService.recaptchaVerifyUrl)).thenReturn(requestBodySpec);
    when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.toEntity(CaptchaResponse.class))
        .thenReturn(new ResponseEntity<>(new CaptchaResponse(), HttpStatus.OK));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> captchaService.verify("invalid-response"));
    assertEquals(ExceptionConstants.CAPTCHA_INVALID, exception.getExceptionCode());
  }

  @Test
  void verify_FalseSuccessResponse_ThrowsBusinessException() {
    var response = new CaptchaResponse();
    response.setSuccess(false);

    when(restClient.post()).thenReturn(requestBodySpec);
    when(requestBodySpec.uri(captchaService.recaptchaVerifyUrl)).thenReturn(requestBodySpec);
    when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestHeadersSpec);
    when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    when(responseSpec.toEntity(CaptchaResponse.class))
        .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

    BusinessException exception =
        assertThrows(BusinessException.class, () -> captchaService.verify("invalid-response"));
    assertEquals(ExceptionConstants.CAPTCHA_INVALID, exception.getExceptionCode());
  }
}
