package com.kadioglumf.dataservice.converter;

import com.kadioglumf.dataservice.enums.Gender;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, String> {

  @Override
  public String convertToDatabaseColumn(Gender attribute) {
    return attribute.toString();
  }

  @Override
  public Gender convertToEntityAttribute(String dbData) {
    return Gender.toAttribute(dbData);
  }
}
