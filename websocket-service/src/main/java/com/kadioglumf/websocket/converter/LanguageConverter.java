package com.kadioglumf.websocket.converter;

import com.kadioglumf.websocket.core.enums.Language;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LanguageConverter implements AttributeConverter<Language, String> {

  @Override
  public String convertToDatabaseColumn(Language attribute) {
    return attribute.getValue();
  }

  @Override
  public Language convertToEntityAttribute(String dbData) {
    return Language.toAttribute(dbData);
  }
}
