package com.kadioglumf.dataservice.util;

import com.kadioglumf.dataservice.constant.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
public class AppUtils {

  public static String generateId(String key) {
    return key + (RandomUtils.nextInt(900000) + 100000);
  }

  public static MultiValueMap<String, String> getSecureHttpHeader(String bearer) {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add(ServiceConstants.TOKEN_HEADER, ServiceConstants.TOKEN_PREFIX + bearer);
    headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    headers.add("Accept-Language", LocaleHolder.getLanguage().getValue());
    return headers;
  }

  public static MultiValueMap<String, String> getHttpHeader() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
    headers.add("Accept-Language", LocaleHolder.getLanguage().getValue());
    return headers;
  }

  public static MultiValueMap<String, String> getMultipartHeader() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);
    return headers;
  }
}
