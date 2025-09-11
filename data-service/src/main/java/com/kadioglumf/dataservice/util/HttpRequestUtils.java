package com.kadioglumf.dataservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtils {

  public static HttpHeaders createNewHttpHeadersWithExtractedMediaTypeFromRequest(
      WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    String contentType = ((ServletWebRequest) request).getRequest().getContentType();
    if (StringUtils.isBlank(contentType)
        || !(MediaType.APPLICATION_JSON.equals(MediaType.parseMediaType(contentType)))
        || MediaType.APPLICATION_XML.equals(MediaType.parseMediaType(contentType))) {
      headers.setContentType(MediaType.APPLICATION_JSON);
    } else {
      headers.setContentType(MediaType.parseMediaType(contentType));
    }
    return headers;
  }
}
