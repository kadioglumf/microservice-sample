package com.kadioglumf.dataservice.util;

import com.kadioglumf.dataservice.enums.Language;
import com.kadioglumf.dataservice.enums.LocationType;
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

  public static String getTranslatedLocationTypeLanguage(LocationType type) {
    try {
      String lang = LocaleContextHolder.getLocale().getLanguage();
      if (Locale.GERMAN.getLanguage().equals(lang)) {
        return switch (type) {
          case ZIP -> "PLZ";
          case CITY -> "Ort";
          case CANTON -> "Kanton";
          case REGION -> "Bezirk";
        };
      } else if (Locale.FRENCH.getLanguage().equals(lang)) {
        return switch (type) {
          case ZIP -> "NPA";
          case CITY -> "Lieu";
          case CANTON -> "Canton";
          case REGION -> "Région";
        };
      } else if (Locale.ITALIAN.getLanguage().equals(lang)) {
        return switch (type) {
          case ZIP -> "NPA";
          case CITY -> "Luogo";
          case CANTON -> "Cantone";
          case REGION -> "Regione";
        };
      } else {
        return switch (type) {
          case ZIP -> "Zip";
          case CITY -> "City";
          case CANTON -> "Canton";
          case REGION -> "Region";
        };
      }
    } catch (Exception ignored) {
    }
    return "-";
  }
}
