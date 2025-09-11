package com.kadioglumf.websocket.core;

import com.google.common.collect.Sets;
import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.exception.ApplicationException;
import com.kadioglumf.websocket.core.exception.ExceptionBundleConstants;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Log4j2
public class StartupCompletedListener implements ApplicationListener<ContextRefreshedEvent> {

  public static final String CLASSPATH_PREFIX = "classpath:";

  private static final String INTER_LOG_MESSAGE_1 =
      " bundle's i18n keys differ: One file contains keys: ";
  private static final String INTER_LOG_MESSAGE_2 = " that the other does not contain";
  private static final String INTER_LOG_MESSAGE_3 = ", and ";
  private static final String INTER_LOG_MESSAGE_4 = " vice versa.";

  @Autowired private ResourceLoader rl;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Spring Context is up and all beans are initialized now...");
    checkBusinessExceptionsI18NKeysAreSynchronizedOrNot();
    checkApplicationExceptionsI18NKeysAreSynchronizedOrNot();
    log.info(
        "All startup checks are done, no important issue found to cancel build/startup process...");
  }

  private void checkBusinessExceptionsI18NKeysAreSynchronizedOrNot() {
    Set<Object> busKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_DEFAULT_PATH,
            false);
    Set<Object> busEnKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_EN_PATH,
            false);
    Set<Object> busFrKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_FR_PATH,
            false);
    Set<Object> busItKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_IT_PATH,
            false);
    checkInternationalizedKeysAreComplete(
        busKeys,
        busEnKeys,
        ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_DEFAULT_PATH,
        true);
    checkInternationalizedKeysAreComplete(
        busKeys,
        busFrKeys,
        ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_DEFAULT_PATH,
        true);
    checkInternationalizedKeysAreComplete(
        busKeys,
        busItKeys,
        ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_DEFAULT_PATH,
        true);
  }

  private void checkApplicationExceptionsI18NKeysAreSynchronizedOrNot() {
    Set<Object> appKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants
                .MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_DEFAULT_PATH,
            false);
    Set<Object> appEnKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_EN_PATH,
            false);
    Set<Object> appFrKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_FR_PATH,
            false);
    Set<Object> appItKeys =
        this.getPropertySetOfaBundle(
            ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_IT_PATH,
            false);
    checkInternationalizedKeysAreComplete(
        appKeys,
        appEnKeys,
        ExceptionBundleConstants.MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_DEFAULT_PATH,
        true);
  }

  // Returns the property set of a bundle represented by its classpath
  private Set<Object> getPropertySetOfaBundle(String path, boolean isIncludeClassPathPrefix) {
    if (StringUtils.isBlank(path)) {
      return new HashSet<>();
    }
    String classpathPrefixedPath = path;
    if (!isIncludeClassPathPrefix) {
      classpathPrefixedPath = CLASSPATH_PREFIX + path;
    }
    Resource resource = rl.getResource(classpathPrefixedPath);
    try {
      if (resource.exists()) {
        return PropertiesLoaderUtils.loadProperties(resource).keySet();
      }
      return new HashSet<>();
    } catch (IOException e) {
      log.error(
          "Error checking all the properties of the bundle: {}. Check whether the file exists on the given path to fix the problem",
          classpathPrefixedPath);
      throw new ApplicationException(ExceptionConstants.SERVER_STARTUP_EXCEPTION);
    }
  }

  private void checkInternationalizedKeysAreComplete(
      Set<Object> toCompareSet1,
      Set<Object> toCompareSet2,
      String pathOfFile,
      boolean throwAndLogError) {
    Set<Object> differenceOf1 = Sets.difference(toCompareSet1, toCompareSet2);
    Set<Object> differenceOf2 = Sets.difference(toCompareSet2, toCompareSet1);
    StringBuilder message = new StringBuilder();
    if (!CollectionUtils.isEmpty(differenceOf1)) {
      String dif1Message =
          differenceOf1.stream()
              .filter(Objects::nonNull)
              .map(Object::toString)
              .collect(Collectors.joining(", "));
      if (StringUtils.isNoneBlank(dif1Message)) {
        message
            .append(pathOfFile)
            .append(INTER_LOG_MESSAGE_1)
            .append(dif1Message)
            .append(INTER_LOG_MESSAGE_2);
      }
    }
    if (!CollectionUtils.isEmpty(differenceOf2)) {
      String dif2Message =
          differenceOf2.stream()
              .filter(Objects::nonNull)
              .map(Object::toString)
              .collect(Collectors.joining(", "));
      if (StringUtils.isNoneBlank(dif2Message)) {
        if (message.isEmpty()) {
          message
              .append(pathOfFile)
              .append(INTER_LOG_MESSAGE_1)
              .append(dif2Message)
              .append(INTER_LOG_MESSAGE_2);
        } else {
          message.append(INTER_LOG_MESSAGE_3).append(dif2Message).append(INTER_LOG_MESSAGE_4);
        }
      }
    }
    if (!message.isEmpty()) {
      if (throwAndLogError) {
        log.error(message.toString());
        throw new ApplicationException(ExceptionConstants.SERVER_STARTUP_EXCEPTION);
      }
      log.warn(message.toString());
    }
  }
}
