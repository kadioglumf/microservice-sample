package com.kadioglumf.websocket.payload;

import com.kadioglumf.websocket.enums.WsInfoType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class WsInfoTypeConverter implements AttributeConverter<WsInfoType, String> {

  @Override
  public String convertToDatabaseColumn(WsInfoType attribute) {
    return attribute.toString();
  }

  @Override
  public WsInfoType convertToEntityAttribute(String dbData) {
    return WsInfoType.toAttribute(dbData);
  }
}
