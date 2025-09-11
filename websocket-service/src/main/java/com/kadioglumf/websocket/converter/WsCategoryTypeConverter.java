package com.kadioglumf.websocket.converter;

import com.kadioglumf.websocket.enums.WsCategoryType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WsCategoryTypeConverter implements AttributeConverter<WsCategoryType, String> {

  @Override
  public String convertToDatabaseColumn(WsCategoryType attribute) {
    return attribute.toString();
  }

  @Override
  public WsCategoryType convertToEntityAttribute(String dbData) {
    return WsCategoryType.toAttribute(dbData);
  }
}
