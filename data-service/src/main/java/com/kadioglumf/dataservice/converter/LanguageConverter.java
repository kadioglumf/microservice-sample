package com.kadioglumf.dataservice.converter;

import com.kadioglumf.dataservice.enums.Language;
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
