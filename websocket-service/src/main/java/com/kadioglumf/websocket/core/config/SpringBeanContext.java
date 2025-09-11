package com.kadioglumf.websocket.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanContext implements ApplicationContextAware {

  private static ApplicationContext context;
  private static String contextPath;

  public SpringBeanContext() {}

  @Value("${server.servlet.context-path:NA}")
  public void setContextPathStatic(String contextPath) {
    SpringBeanContext.contextPath = contextPath;
  }

  public static <T> T getBean(Class<T> bean) {
    return context.getBean(bean);
  }

  public static String getApplicationContextPath() {
    return contextPath.replace("/", "");
  }

  public static <T extends Object> T getBean(String beanName) {
    return (T) context.getBean(beanName);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringBeanContext.context = applicationContext;
  }
}
