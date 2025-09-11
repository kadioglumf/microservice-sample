package com.kadioglumf.dataservice.service;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import com.kadioglumf.dataservice.payload.response.captcha.CaptchaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class CaptchaService {

  private final RestClient externalRestClient;

  @Value("${google.recaptcha.secret.key}")
  public String recaptchaSecret;

  @Value("${google.recaptcha.verify.url}")
  public String recaptchaVerifyUrl;

  public void verify(String response) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("secret", recaptchaSecret);
    param.add("response", response);

    CaptchaResponse recaptchaResponse;
    recaptchaResponse =
        externalRestClient
            .post()
            .uri(recaptchaVerifyUrl)
            .body(param)
            .retrieve()
            .toEntity(CaptchaResponse.class)
            .getBody();
    if (recaptchaResponse == null
        || recaptchaResponse.getSuccess() == null
        || !recaptchaResponse.getSuccess()) {
      throw new BusinessException(ExceptionConstants.CAPTCHA_INVALID);
    }
  }
}
