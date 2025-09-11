package com.kadioglumf.authservice.converter;

import com.kadioglumf.authservice.enums.GenderEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<GenderEnum, String> {

  @Override
  public String convertToDatabaseColumn(GenderEnum attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.toString();
  }

  @Override
  public GenderEnum convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return GenderEnum.toAttribute(dbData);
  }
}
