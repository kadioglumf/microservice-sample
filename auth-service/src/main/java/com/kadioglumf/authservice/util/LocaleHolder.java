package com.kadioglumf.authservice.util;

import com.kadioglumf.authservice.core.enums.Language;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;

public class LocaleHolder {

  public static Language getLanguage() {
    try {
      String lang = LocaleContextHolder.getLocale().getLanguage();
      if (Locale.ENGLISH.getLanguage().equals(lang)) {
        return Language.EN;
      } else if (Locale.FRENCH.getLanguage().equals(lang)) {
        return Language.FR;
      } else if (Locale.GERMAN.getLanguage().equals(lang)) {
        return Language.DE;
      } else if (Locale.ITALIAN.getLanguage().equals(lang)) {
        return Language.IT;
      }
    } catch (Exception ignored) {
    }
    return Language.DE;
  }
}
