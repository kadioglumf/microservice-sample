package com.kadioglumf.authservice.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMetaData {

  @Value("${spring.application.name:Unknown}")
  private String applicationName;

  @Value("${server.servlet.context-path:Unknown}")
  private String serverServletContextPath;

  public String getApplicationName() {
    String toReturn = applicationName;
    if ("Unknown".equals(applicationName)) {
      toReturn = serverServletContextPath;
    }
    return toReturn.startsWith("/") ? toReturn.substring(1, toReturn.length()) : toReturn;
  }
}
