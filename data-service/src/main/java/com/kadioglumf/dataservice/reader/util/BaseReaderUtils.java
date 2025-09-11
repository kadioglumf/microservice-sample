package com.kadioglumf.dataservice.reader.util;

import java.util.List;

public abstract class BaseReaderUtils {

  protected abstract <T> List<T> read(Class<T> clazz) throws Exception;
}
