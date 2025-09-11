package com.kadioglumf.dataservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class MediaTypeConfig implements WebMvcConfigurer {

  private final ObjectMapper objectMapper;

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    ReadOnlyMultipartFormDataEndpointConverter converter =
        new ReadOnlyMultipartFormDataEndpointConverter(objectMapper);
    List<MediaType> supportedMediaTypes = new ArrayList<>();
    supportedMediaTypes.addAll(converter.getSupportedMediaTypes());
    supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
    converter.setSupportedMediaTypes(supportedMediaTypes);

    converters.add(converter);
  }
}

class ReadOnlyMultipartFormDataEndpointConverter extends MappingJackson2HttpMessageConverter {

  public ReadOnlyMultipartFormDataEndpointConverter(ObjectMapper objectMapper) {
    super(objectMapper);
  }

  @Override
  public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (requestAttributes == null) {
      return false;
    }
    HandlerMethod handlerMethod =
        (HandlerMethod)
            requestAttributes.getAttribute(
                HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
    if (handlerMethod == null) {
      return false;
    }
    RequestMapping requestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
    if (requestMapping == null) {
      return false;
    }
    if (requestMapping.consumes().length != 1
        || !MediaType.MULTIPART_FORM_DATA_VALUE.equals(requestMapping.consumes()[0])) {
      return false;
    }
    return super.canRead(type, contextClass, mediaType);
  }

  @Override
  protected boolean canWrite(MediaType mediaType) {
    // This converter is only be used for requests.
    return false;
  }
}
