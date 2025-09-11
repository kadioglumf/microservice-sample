package com.kadioglumf.dataservice.util;

import java.util.Set;
import java.util.stream.Collectors;

public class DataCacheKeyUtils {

  public static String sortedUserIdsKey(Set<Long> userIds) {
    return userIds.stream().sorted().map(String::valueOf).collect(Collectors.joining(","));
  }
}
