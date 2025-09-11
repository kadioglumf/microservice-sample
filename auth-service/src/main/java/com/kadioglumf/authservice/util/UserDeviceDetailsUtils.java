package com.kadioglumf.authservice.util;

import com.kadioglumf.authservice.enums.IpTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserDeviceDetailsUtils {

  private static final String UNKNOWN_IP_ADDRESS = "unknown";
  private static final String ORIGIN = "origin";
  private static final String USER_AGENT = "user-agent";
  private static final String ACCEPT_LANGUAGE = "Accept-Language";

  private static final String[] IP_HEADER_CANDIDATES = {
    "X-Forwarded-For",
    "Proxy-Client-IP",
    "WL-Proxy-Client-IP",
    "HTTP_X_FORWARDED_FOR",
    "HTTP_X_FORWARDED",
    "HTTP_X_CLUSTER_CLIENT_IP",
    "HTTP_CLIENT_IP",
    "HTTP_FORWARDED_FOR",
    "HTTP_FORWARDED",
    "HTTP_VIA",
    "REMOTE_ADDR"
  };

  private static final Map<IpTypeEnum, Integer> ipTypes =
      Map.of(
          IpTypeEnum.CLIENT, 0,
          IpTypeEnum.ORIGIN, 1);

  public static String getIpAddr(IpTypeEnum ipType) {
    String ipAddress = "";
    if (RequestContextHolder.getRequestAttributes() == null) {
      return "Unknown User";
    }

    HttpServletRequest request = getHttpServletRequest();

    for (String header : IP_HEADER_CANDIDATES) {
      ipAddress = request.getHeader(header);
      if (ipAddress != null
          && ipAddress.length() != 0
          && !UNKNOWN_IP_ADDRESS.equalsIgnoreCase(ipAddress)) {
        break;
      }
    }
    if (ipAddress == null
        || ipAddress.length() == 0
        || UNKNOWN_IP_ADDRESS.equalsIgnoreCase(ipAddress)) {
      ipAddress = request.getRemoteAddr();
    }
    if ("0:0:0:0:0:0:0:1".equals(ipAddress)) {
      // added for localhost domain
      ipAddress = "127.0.0.1";
    }
    if (ipTypes.get(ipType) != null && ipAddress.contains(",")) {
      return ipAddress.split(",")[ipTypes.get(ipType)].replaceAll("\\s", "");
    }
    return ipAddress;
  }

  public static String getOrigin() {
    HttpServletRequest request = getHttpServletRequest();
    if (request == null || request.getHeader(ORIGIN) == null) {
      return "UNKNOWN_ORIGIN";
    }
    return request.getHeader(ORIGIN);
  }

  private static HttpServletRequest getHttpServletRequest() {
    if (RequestContextHolder.getRequestAttributes() != null) {
      return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    return null;
  }

  public static String getUserAgent() {
    HttpServletRequest request = getHttpServletRequest();
    if (request == null || request.getHeader(USER_AGENT) == null) {
      return "UNKNOWN_USER_AGENT";
    }
    return request.getHeader(USER_AGENT);
  }

  public static Locale getUserLocale() {
    HttpServletRequest request = getHttpServletRequest();
    if (request == null) {
      return Locale.of("de", "CH");
    }
    String acceptLanguage = request.getHeader(ACCEPT_LANGUAGE);
    if (StringUtils.isBlank(acceptLanguage)) {
      return Locale.of("de", "CH");
    }
    String[] language = acceptLanguage.split("-");
    if (language.length < 2) {
      return Locale.of(language[0]);
    }
    return Locale.of(language[0], language[1]);
  }
}
