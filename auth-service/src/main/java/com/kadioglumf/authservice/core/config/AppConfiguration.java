package com.kadioglumf.authservice.core.config;

import static com.kadioglumf.authservice.core.exception.ExceptionBundleConstants.*;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@Log4j2
public class AppConfiguration {
  private final List<String> externalApplicationExceptionBundles = new ArrayList<>();
  private final List<String> externalBusinessExceptionBundles = new ArrayList<>();

  @Bean
  public MessageSource applicationExceptionMessageSource() {
    externalApplicationExceptionBundles.add(
        CLASSPATH_FOR_EXCEPTION_PROPERTIES + MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS);
    return getReloadableResourceBundleMessageSource(
        externalApplicationExceptionBundles.toArray(String[]::new));
  }

  @Bean
  public MessageSource businessExceptionMessageSource() {
    externalBusinessExceptionBundles.add(
        CLASSPATH_FOR_EXCEPTION_PROPERTIES + MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS);
    return getReloadableResourceBundleMessageSource(
        externalBusinessExceptionBundles.toArray(String[]::new));
  }

  private ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource(
      String... message) {
    final ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(message);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(0);
    return messageSource;
  }
}
